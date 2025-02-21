<template>
  <div class="main-content">
    <div style="width: 60%; background-color: white; min-height: 1000px; margin: 20px auto; border-radius: 5px">
      <div style="padding: 15px 20px">
        <el-row :gutter="20">
          <el-col :span="12">
            <img :src="goodsData.img" alt="" style="width: 100%; height: 400px; border-radius: 5px">
          </el-col>
          <el-col :span="12">
            <div style="font-size: 20px; font-weight: 900; overflow: hidden; text-overflow: ellipsis;display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical">{{goodsData.name}}</div>
            <div style="color: rgb(102, 102, 102); font-size: 14px; margin-top: 5px">Sales: {{goodsData.count}}</div>
            <div style="color: #2a60c9; margin-top: 15px">Price: <span style="font-size: 20px">{{goodsData.price}} $</span></div>
            <div style="margin-top: 20px">
              <img src="@/assets/imgs/right.png" alt="" style="width: 60%; height: 130px; border-radius: 15px">
            </div>
              <div style="color: rgb(102, 102, 102); font-size: 14px; margin-top: 20px">Merchant: {{goodsData.businessName}}</div>
            <div style="color: rgb(102, 102, 102); font-size: 14px; margin-top: 20px">Type: <a href="#" @click="navTo('/front/type?id=' + goodsData.typeId)">{{goodsData.typeName}}</a></div>
            <div style="margin-top: 20px">
              <el-button type="warning">Add to Cart</el-button>
              <el-button type="warning">Add to Favourite</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
      <div style="padding: 15px 20px">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="Deatils" name="first">
            <div style="padding: 10px 175px" v-html="goodsData.description"></div>
          </el-tab-pane>
          <el-tab-pane label="Comments" name="second">Comments</el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    let goodsId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      goodsId: goodsId,
      goodsData: {}
    }
  },
  mounted() {
    this.loadGoods()
  },
  // methods: All click events or other function definition areas of this page
  methods: {
    loadGoods() {
      this.$request.get('/goods/selectById?id=' + this.goodsId).then(res =>{
        if(res.code === '200') {
          this.goodsData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleClick(tab, event) {
      this.activeName = tab.name
    },
    navTo(url){
      location.href = url
    }
  }
}
</script>
