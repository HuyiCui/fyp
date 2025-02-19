<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter the product name to search" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Query</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Product publish</el-button>
      <el-button type="danger" plain @click="delBatch">Batch Deletion</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column>
        <el-table-column label="img">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; " v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="product name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="product description" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button type="success" @click="viewEditor(scope.row.description)">Click to check</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="product price" show-overflow-tooltip></el-table-column>
        <el-table-column prop="unit" label="product unit" show-overflow-tooltip></el-table-column>
        <el-table-column prop="typeName" label="type" show-overflow-tooltip></el-table-column>
        <el-table-column prop="businessName" label="business" show-overflow-tooltip></el-table-column>
        <el-table-column prop="count" label="sales number" show-overflow-tooltip></el-table-column>
        <el-table-column label="operate" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">Edit</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>Delete</el-button>
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


    <el-dialog title="message" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close :show-close="false">
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="Product picture">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">Upload Pictures</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="product name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="price" label="product price">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="typeId" label="product type">
          <el-select v-model="form.typeId" placeholder="please select type" style="width: 100%">
            <el-option v-for="item in typeData" :label="item.name" :value="item.id" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="unit" label="product unit">
          <el-input v-model="form.unit" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="product description">
          <div id="editor" style="width: 100%"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">Cancel</el-button>
        <el-button type="primary" @click="save">Sure</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Product Description" :visible.sync="editorVisible" width="50%">
      <div v-html="this.viewData" class="w-e-text"></div>
    </el-dialog>
  </div>
</template>

<script>
import E from 'wangeditor'

let editor
function initWangEditor(content) { setTimeout( () =>{
  if (!editor) {
    editor = new E('#editor')
    editor.config.placeholder = 'please enter the content'
    editor.config.uploadFileName = 'file'
    editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
    editor.create()
  }
  editor.txt.html(content)
}, 0)
}

export default {
  name: "Notice",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        title: [
          {required: true, name: 'Please enter a product name', trigger: 'blur'},
        ],
        content: [
          {required: true, img: 'Please upload a product icon', trigger: 'blur'},
        ]
      },
      ids: [],
      typeData: [],
      viewData: null,
    }
  },
  created() {
    this.load(1)
    this.loadType()
  },
  methods: {
    loadType(){
      this.$request.get('/type/selectAll').then(res => {
        if(res.code === '200') {
          this.typeData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      if('check passed' !== this.user.status) {
        this.$message.warning('havent passed')
        return
      }
      this.form = {}
      initWangEditor('')
      this.fromVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      initWangEditor(this.form.description || '')
      this.fromVisible = true
    },
    viewEditor(content){
      this.viewData = content
      this.editorVisible = true
    },
    cancel(){
      this.fromVisible=false
      location.href = '/goods'
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.description = editor.txt.html()
          this.$request({
            url: this.form.id ? '/goods/update' : '/goods/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('Saved successfully')
              //this.load(1)
              this.fromVisible = false
              location.href = '/goods'
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('Are you sure you want to delete?？', 'Confirm Delete', {type: "warning"}).then(response => {
        this.$request.delete('/goods/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('Please select data')
        return
      }
      this.$confirm('Are you sure you want to delete this data in batches?', 'Confirm Delete', {type: "warning"}).then(response => {
        this.$request.delete('/goods/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
    handleAvatarSuccess(response, file, fileList) {
      this.form.img = response.data
    },
  }
}
</script>

<style scoped>

</style>
