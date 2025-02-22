<template>
  <div class="main-content">
    <div style="width: 70%; background-color: white; margin: 30px auto; border-radius: 5px">
      <div style="padding-bottom: 8px">
        <div style="display: flex; font-size: 18px; color: #000000FF; line-height: 80px; border-bottom: #cccccc 1px solid;">
          <div style="margin-left: 20px; flex: 6">My Address</div>
          <div style="flex: 1;">
            <el-button type="info" round @click="addAddress">Add Address</el-button>
          </div>
        </div>
        <div style="margin: 20px 0; padding: 0 50px">
          <div class="table">
            <el-table :data="addressData" strip>
              <el-table-column prop="username" label="Name"></el-table-column>
              <el-table-column prop="useraddress" label="Address"></el-table-column>
              <el-table-column prop="phone" label="Phone"></el-table-column>
              <el-table-column label="Operate" align="center" width="180">
                <template v-slot="scope">
                  <el-button size="mini" type="primary" plain @click="editAddress(scope.row)">Edit</el-button>
                  <el-button size="mini" type="danger" plain @click="del(scope.row.id)">Delete</el-button>
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
    <el-dialog title="Address Information" :visible.sync="formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username" label="Name">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="useraddress" label="Address">
          <el-input v-model="form.useraddress" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="phone" label="Phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Sure</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      addressData: [],
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items displayed per page
      total: 0,
      formVisible: false,
      form: {},
      rules: {
        username: [
          {required: true, message: 'Please enter a name', trigger: 'blur'},
        ],
        useraddress: [
          {required: true, message: 'Please enter an effective address', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: 'Please enter an effective address number', trigger: 'blur'},
        ],
      },
    }
  },
  mounted() {
    this.loadAddress(1)
  },
  // methods: All click events or other function definition areas of this page
  methods: {
    addAddress(){
      this.form = {}
      this.formVisible = true
    },
    editAddress(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.formVisible = true
    },
    loadAddress(pageNum){
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/address/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if(res.code === '200') {
          this.addressData = res.data?.list
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
      this.$request.delete('/address/delete/' + id).then(res =>{
        if(res.code === '200') {
          this.$message.success("Successful")
          this.loadAddress(1)
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum){
      this.loadAddress(pageNum)
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.userId = this.user.id
          this.$request({
            url: this.form.id ? '/address/update' : '/address/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('Saved successfully')
              this.loadAddress(1)
              this.formVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>
