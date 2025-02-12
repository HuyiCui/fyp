<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter a title to search" style="width: 200px" v-model="title"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Query</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Added</el-button>
      <el-button type="danger" plain @click="delBatch">Batch Deletion</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="title" label="title" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="content" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="time"></el-table-column>
        <el-table-column prop="user" label="user"></el-table-column>

        <el-table-column label="operate" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">edit</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
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


    <el-dialog title="message" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="title" label="title">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="content">
          <el-input type="textarea" :rows="5" v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">cancel</el-button>
        <el-button type="primary" @click="save">confirm</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "Notice",
  data() {
    return {
      tableData: [],  // All data
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items displayed per page
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        title: [
          {required: true, message: 'please enter title', trigger: 'blur'},
        ],
        content: [
          {required: true, message: 'please enter content', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {   // New data
      this.form = {}  // Clear data when adding new data
      this.fromVisible = true   // Open pop-up
    },
    handleEdit(row) {   // Edit data
      this.form = JSON.parse(JSON.stringify(row))  // Assign values to the form object and make sure to copy the data deeply.
      this.fromVisible = true   // Open pop-up
    },
    save() {   // The logic triggered by the save button will trigger the addition or update
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/notice/update' : '/notice/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // Indicates successful saving
              this.$message.success('saved successfully')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // Error message pops up
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('are you sure to delete？', 'confirm delete', {type: "warning"}).then(response => {
        this.$request.delete('/notice/delete/' + id).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('successfully')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // Error message pops up
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // All currently selected row data
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // Batch Deletion
      if (!this.ids.length) {
        this.$message.warning('select item')
        return
      }
      this.$confirm('Are you sure you want to delete this data in batches?', 'Confirm Delete', {type: "warning"}).then(response => {
        this.$request.delete('/notice/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // Error message pops up
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // Pagination Query
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/notice/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
