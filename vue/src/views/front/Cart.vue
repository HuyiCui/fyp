<template>
  <div class="main-content">
    <div style="width: 70%; background-color: white; margin: 30px auto; border-radius: 5px">
      <div style="padding-bottom: 8px">
        <div style="display: flex; font-size: 18px; color: #000000FF; line-height: 80px; border-bottom: #cccccc 1px solid;">
          <div style="margin-left: 20px; flex: 1;">My Cart ({{goodsData.length}})</div>
          <div style="flex: 2; text-align: right">
            <el-select v-model="addressId" placeholder="Please select your address" style="width: 70%">
              <el-option v-for="item in addressData" :label="item.username + ' - ' + item.useraddress" :value="item.id"></el-option>
            </el-select>
          </div>
          <div style="flex: 1; font-size: 16px; text-align: right; padding-right: 20px">
            Selected: $ {{ totalPrice }} <el-button type="info" round @click="pay()">Order</el-button>
          </div>
        </div>
        <div style="margin: 20px 0; padding: 0 50px">
          <div class="table">
            <el-table :data="goodsData" strip @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column label="Image">
                <template v-slot="scope">
                  <el-image style="width: 80px; height: 60px; border-radius: 3px" v-if="scope.row.goodsImg"
                            :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
                </template>
              </el-table-column>
              <el-table-column prop="goodsName" label="Name">
                <template v-slot="scope">
                  <a :href="'/front/detail?id=' + scope.row.goodsId">{{scope.row.goodsName}}</a>
                </template>
              </el-table-column>
              <el-table-column prop="businessName" label="Merchant">
                <template v-slot="scope">
                  <a :href="'/front/business?id=' + scope.row.businessId">{{scope.row.businessName}}</a>
                </template>
              </el-table-column>
              <el-table-column prop="goodsPrice" label="Price"></el-table-column>
              <el-table-column prop="num" label="Number">
                <template v-slot="scope">
                  <el-input-number v-model="scope.row.num" @change="handleChange(scope.row)" :min="1" style="width: 100px"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="Operate" align="center" width="180">
                <template v-slot="scope">
                  <el-button size="mini" type="danger" plain @click="del(scope.row.id)">Remove</el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="pagination" style="margin-top: 20px">
              <el-pagination
                  background
                  @current-change="handleCurrentChange"
                  :current-page="pageNum"
                  :page-sizes="[5, 10, 20]"
                  :page-size="pageSize"
                  layout="total, prev, pager, next"
                  :total="total">
              </el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      goodsData: [],
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items displayed per page
      total: 0,
      addressId: null,
      addressData: [],
      totalPrice: 0,
      selectedData: [],
    }
  },
  mounted() {
    this.loadGoods(1)
    this.loadAddress()
  },
  // methods: All click events or other function definition areas of this page
  methods: {
    loadAddress(){
      this.$request.get('/address/selectAll').then(res => {
        if(res.code === '200') {
          this.addressData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadGoods(pageNum){
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/cart/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if(res.code === '200') {
          this.goodsData = res.data?.list
          this.total = res.data?.total
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url){
      location.href = url
    },
    del(id){
      this.$request.delete('/cart/delete/' + id).then(res =>{
        if(res.code === '200') {
          this.$message.success("Successful")
          this.loadGoods(1)
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum){
      this.loadGoods(pageNum)
    },
    handleSelectionChange(rows){
      this.selectedData = rows
      let price = 0
      //count the total price
      this.selectedData.forEach(item => {
        price += (item.goodsPrice * item.num)
      })
      this.totalPrice = price
    },
    handleChange(row){
      this.totalPrice = 0
      this.selectedData.forEach(item => {
        this.totalPrice += item.goodsPrice * item.num
      })
    },
    pay(){
      if (!this.addressId) {
        this.$message.warning('Please select the address')
        return
      }
      if(!this.selectedData || this.selectedData.length === 0){
        this.$message.warning('Please select the Product')
        return
      }
      let data = {
        userId: this.user.id,
        addressId: this.addressId,
        price: this.totalPrice,
        status: 'Processing',
        cartData: this.selectedData,
      }
      this.$request.post('orders/add', data).then(res =>{
        if (res.code === '200') {
          this.$message.success('Successful')
          this.loadGoods(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
