<template>
  <div class="main-content">
    <div style="height: 60px; background-color: rgb(223,223,223)"></div>
    <div style="display: flex">
      <div class="left"></div>
      <div style="width: 66%; background-color: white; height: 1000px">
        <div style="color: rgb(53,84,118); margin: 15px 0 15px 18px; font-size: 16px; font-weight: bold">Second-hand Market</div>
        <div style="display: flex; margin: 0 25px">
          <div style="flex: 2; height: 800px">
            <div style="display: flex; color: rgb(102,102,102); margin: 14px 0" v-for="item in typeData">
              <img :src="item.img" alt="" style="height: 20px; width: 20px">
              <div style="margin-left: 10px; font-size: 14px">{{ item.name }}</div>
            </div>
          </div>
          <div style="flex: 5; height: 800px; background-color: rgb(186,186,186)">
            <div style="display: flex; color: rgb(102,102,102); margin: 14px 0">
              <img src="" alt="">
              <div style="margin-left: 10px; font-size: 14px">Show Personal Recommendation</div>
            </div>
          </div>
          <div style="flex: 3; height: 800px; background-color: rgb(232,232,232)">
            <div style="display: flex; color: rgb(102,102,102); margin: 14px 0">
              <img src="" alt="">
              <div style="margin-left: 10px; font-size: 14px">Personal Details</div>
            </div>
          </div>
        </div>
      </div>
      <div class="right"></div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      typeData: []
    }
  },
  mounted() {
    this.loadType()
  },
  // methods: All click events or other function definition areas of this page
  methods: {
    loadType() {
      this.$request.get('/type/selectAll').then(res => {
        if(res.code == '200') {
          this.typeData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.main-content{
  min-height: 100vh;
  background-size: 100%;
}
.left {
  width: 17%;
  background-repeat: no-repeat;
  background-color: rgb(223,223,223);
  height: 10000px;
}
.right {
  width: 17%;
  background-repeat: no-repeat;
  background-color: rgb(223,223,223);
  height: 10000px;
}
.el-col-5{
  width: 20%;
  max-width: 20%;
  padding: 10px 10px;
}
</style>