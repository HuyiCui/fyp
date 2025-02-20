<template>
  <div class="main-content">
    <div style="height: 60px; background-color: rgb(223,223,223)"></div>
    <div style="display: flex">
      <div class="left" ></div>
      <div style="width: 66%; background-color: white; margin-bottom: 50px;">
        <div style="color: rgb(53,84,118); margin: 15px 0 15px 18px; font-size: 16px; font-weight: bold">Second-hand Market</div>
        <div style="display: flex; margin: 0 25px; height: 545px" >
          <div style="flex: 2; height: 800px">
            <div style="display: flex; color: rgb(102,102,102); margin: 14px 0" v-for="item in typeData">
              <img :src="item.img" alt="" style="height: 20px; width: 20px">
              <div style="margin-left: 10px; font-size: 14px">{{ item.name }}</div>
            </div>
          </div>
          <div style="flex: 5; margin-top: 15px">
            <div>
              <el-carousel height="300px" style="border-radius: 5px">
                <el-carousel-item v-for="item in carousel_top">
                  <img :src="item" alt="" style="width: 100%; height: 300px; border-radius: 5px">
                </el-carousel-item>
              </el-carousel>
            </div>
            <div style="margin-top: 30px; display: flex">
              <div style="flex: 1">
                <el-carousel height="300px" style="border-radius: 5px">
                  <el-carousel-item v-for="item in carousel_left">
                    <img :src="item" alt="" style="width: 100%; height: 200px; border-radius: 5px">
                  </el-carousel-item>
                </el-carousel>
              </div>
              <div style="flex: 1; margin-left: 5px">
                <el-carousel height="300px" style="border-radius: 5px">
                  <el-carousel-item v-for="item in carousel_right">
                    <img :src="item" alt="" style="width: 100%; height: 200px; border-radius: 5px">
                  </el-carousel-item>
                </el-carousel>
              </div>
            </div>
          </div>
          <div style="flex: 3; background-color: rgb(243, 243, 243); margin-top: 15px; margin-left: 15px; border-radius: 5px">
            <div style="text-align: center; margin-top: 30px">
              <img @click="navToPerson" :src="user.avatar" alt="" style="width: 80px; height: 80px">
              <div style="margin-top: 10px">Hi, {{user.name}}</div>
            </div>
            <div style="margin-top: 20px; padding: 0 15px">
              <img src="@/assets/imgs/right.png" alt="" style="height: 150px; width: 100%; border-radius: 10px">
            </div>
            <div style="margin: 20px 10px 10px 10px; width: 250px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis">
              <i class="el-icon-bell"></i>
              <span style="color: #666666">Notice: </span>
              <span style="color: #666666">{{top}}</span>
            </div>
            <div style="display: flex; margin-top: 50px">
              <div style="flex: 1; text-align: center">
                <img src="@/assets/imgs/favourite.png" alt="" style="height: 25px; width: 25px">
                <div>Favourites</div>
              </div>
              <div style="flex: 1; text-align: center">
                <img src="@/assets/imgs/address.png" alt="" style="height: 25px; width: 25px">
                <div>Address</div>
              </div>
              <div style="flex: 1; text-align: center">
                <img src="@/assets/imgs/cart.png" alt="" style="height: 25px; width: 25px">
                <div>Cart</div>
              </div>
              <div style="flex: 1; text-align: center">
                <img src="@/assets/imgs/order.png" alt="" style="height: 25px; width: 25px">
                <div>Order</div>
              </div>
            </div>
          </div>
        </div>
        <div style="margin: 40px 0 0 15px; height: 40px; background-color: lightskyblue; font-size: 20px; color: white; width: 130px; font-weight: bold; height: 40px; line-height: 40px; text-align: center; border-radius: 10px">Hot Selling</div>
        <div style="margin: 10px 5px 0 5px">
          <el-row>
            <el-col :span="5" v-for="item in goodsData">
              <img :src="item.img" alt="" style="width: 100%; height: 175px; border-radius: 10px; border: #cccccc 1px solid; ">
              <div style="margin-top: 10px; font-weight: 500; font-size: 16px; width: 180px; color: black; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{item.name}}</div>
              <div style="margin-top: 5px; font-size: 20px; color: darkblue">${{item.price}}</div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="right"></div>
    </div>
  </div>
</template>

<script>

import user from "@/views/manager/User.vue";

export default {
  computed: {
    user() {
      return user
    }
  },

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      typeData: [],
      top: null,
      notice: [],
      goodsData: [],
      carousel_top: [
          require('@/assets/imgs/carousel-1.png'),
          require('@/assets/imgs/carousel-2.png'),
          require('@/assets/imgs/carousel-3.png')
      ],
      carousel_left: [
        require('@/assets/imgs/carousel-4.png'),
        require('@/assets/imgs/carousel-5.png'),
        require('@/assets/imgs/carousel-6.png')
      ],
      carousel_right: [
        require('@/assets/imgs/carousel-7.png'),
        require('@/assets/imgs/carousel-8.png'),
        require('@/assets/imgs/carousel-9.png')
      ],
    }
  },
  mounted() {
    this.loadType()
    this.loadNotice()
    this.loadGoods()
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
    },
    loadNotice(){
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if(this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if(i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    loadGoods(){
      this.$request.get('/goods/selectTop15').then(res => {
        if(res.code === '200') {
          this.goodsData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navToPerson(){
      location.href = '/front/person'
    },
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