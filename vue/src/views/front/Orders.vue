<template>
  <div class="main-content">
    <div style="width: 80%; background-color: white; margin: 30px auto; border-radius: 5px">
      <div style="padding-bottom: 8px">
        <div style="font-size: 18px; color: #000000FF; line-height: 80px; border-bottom: #cccccc 1px solid;">
          <div style="margin-left: 20px">My Orders ({{ordersData.length}})</div>
        </div>
        <div style="margin: 20px 0; padding: 0 50px">
          <div class="table">
            <el-table :data="ordersData" strip>
              <el-table-column label="Image">
                <template v-slot="scope">
                  <el-image style="width: 80px; height: 60px; border-radius: 3px" v-if="scope.row.goodsImg"
                            :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
                </template>
              </el-table-column>
              <el-table-column prop="orderId" label="OrderId"></el-table-column>
              <el-table-column prop="goodsName" label="Name"></el-table-column>
              <el-table-column prop="businessName" label="Merchant"></el-table-column>
              <el-table-column prop="goodsPrice" label="Price"></el-table-column>
              <el-table-column prop="num" label="Number"></el-table-column>
              <el-table-column prop="price" label="Total"></el-table-column>
              <el-table-column prop="username" label="Customer"></el-table-column>
              <el-table-column prop="useraddress" label="Address"></el-table-column>
              <el-table-column prop="phone" label="Phone"></el-table-column>
              <el-table-column prop="status" label="Status"></el-table-column>
              <el-table-column label="Operate" align="center" width="180">
                <template v-slot="scope">
                  <el-button size="mini" type="primary" v-if="scope.row.status === 'Delivered'" plain @click="updateStatus(scope.row, 'Finished')">Confirm</el-button>
                  <el-button plain type="primary" size="mini" v-if="scope.row.status === 'Finished'" @click="addComment(scope.row)" >Coment</el-button>
                  <el-button size="mini" type="danger" plain @click="del(scope.row.id)">Cancel</el-button>
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
    <el-dialog title="Please leave the comment" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Content" prop="username">
          <el-input type="textarea" v-model="form.content" placeholder="Content"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      ordersData: [],
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items displayed per page
      total: 0,
      form: {},
      fromVisible: false,
    }
  },
  mounted() {
    this.loadOrders(1)
  },
  // methods: All click events or other function definition areas of this page
  methods: {
    loadOrders(pageNum){
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if(res.code === '200') {
          this.ordersData = res.data?.list
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
      this.$request.delete('/orders/delete/' + id).then(res =>{
        if(res.code === '200') {
          this.$message.success("Successful")
          this.loadOrders(1)
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum){
      this.loadOrders(pageNum)
    },
    updateStatus (row, status) {
      this.form = row;
      this.form.status = status;
      this.$request.put('/orders/update', this.form).then(res => {
        if(res.code === '200') {
          this.$message.success('successful')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    addComment(row) {
      this.fromVisible = true
      this.form = row

    },
    save(){
      let data = {
        userId: this.user.id,
        businessId: this.form.businessId,
        goodsId: this.form.goodsId,
        content: this.form.content,
      }
      this.$request.post('/comment/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('successful')
          this.fromVisible = false
          this.updateStatus(this.form, 'Rated')
          this.form = {}
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
