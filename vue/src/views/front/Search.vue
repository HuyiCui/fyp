<template>
  <div class="main-content">
    <div style=" display: flex; width: 70%; background-color: white; margin: 30px auto; border-radius: 5px">
      <div style="flex: 1; padding: 0 20px">
        <div style="font-size: 18px; color: #000000FF; line-height: 80px; border-bottom: #cccccc 1px solid">Result of "{{name}}"</div>
        <div style="margin: 20px 0">
          <el-row :gutter="20">
            <el-col :span="6" style="margin-bottom: 20px" v-for="item in goodsData">
              <img @click="navTo('/front/detail?id=' + item.id)" :src="item.img" alt="" style="width: 100%; height: 175px; border-radius: 10px; border: #cccccc 1px solid; ">
              <div style="margin-top: 10px; font-weight: 500; font-size: 16px; width: 180px; color: black; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{item.name}}</div>
              <div style="margin-top: 5px; font-size: 20px; color: darkblue">${{item.price}}</div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div style="width: 250px; padding: 0 20px; border-left: #cccccc 1px solid">
        <div style="font-size: 18px; color: #000000FF; line-height: 80px; border-bottom: #cccccc 1px solid;">Recommandation</div>
        <div style="margin: 20px 0">
          <div style="margin-bottom: 20px" v-for="item in recommendData">
            <img @click="navTo('/front/detail?id=' + item.id)" :src="item.img" alt="" style="width: 100%; height: 180px; border-radius: 10px; border: #cccccc 1px solid; ">
            <div style="margin-top: 10px; font-weight: 500; font-size: 16px; width: 180px; color: black; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{item.name}}</div>
            <div style="margin-top: 5px; font-size: 20px; color: darkblue">${{ item.price }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    let name = this.$route.query.name
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      name: name,
      goodsData: [],
      recommendData: [],
    }
  },
  mounted() {
    this.loadGoods()
    this.loadRecommend()
  },
  // methods: All click events or other function definition areas of this page
  methods: {
    loadRecommend() {
      this.$request.get('/goods/recommend').then(res => {
        if(res.code === '200') {
          this.recommendData = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    loadGoods(){
      this.$request.get('/goods/selectByName?name=' + this.name).then(res => {
        if (res.code === '200') {
          this.goodsData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url){
      location.href = url
    }
  }
}
</script>
