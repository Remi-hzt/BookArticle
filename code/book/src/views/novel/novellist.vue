<template>
    <div>
      <el-row :gutter="10">
        <el-col :span="4">
          <el-input id="searchname" placeholder="请输入文章名称" v-model="seanovelname" style="width: 180px;"></el-input>
        </el-col>
        <el-col :span="4">
          <el-input id="searchauthor" placeholder="请输入作者" v-model="seaauthor" style="width: 180px;"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button id="search" type="primary" @click="search">搜索</el-button>
        </el-col>
      </el-row>
      <el-table :data="novels" stripe>
        <el-table-column prop="id" label="文章编号"></el-table-column>
        <el-table-column prop="novelname" label="文章名称"></el-table-column>
        <el-table-column prop="novelbrief" label="文章简介"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="opentime" label="发布时间"></el-table-column>
        <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="info" icon="el-icon-view"  @click="infoNovel(scope.row)" circle></el-button>
              <el-button type="primary" icon="el-icon-edit"  @click="editNovel(scope.row)" circle></el-button>
              <el-button type="danger" icon="el-icon-delete"  @click="deleteNovel(scope.row)" circle></el-button>
            </template>
        </el-table-column>
      </el-table>
      <el-dialog  title="内容" :visible.sync="dialogVisible" :before-close="handleClose" :close-on-click-modal ="false">
        <div v-html="novelInfo"></div>
      </el-dialog>
      <el-dialog :visible.sync="novelEditDialog" :before-close="novelhandleClose" :close-on-click-modal ="false">
          <el-form :model="novelForm" :rules="rules" ref="novelForm" label-width="120px">
            <el-form-item label="文章名称" prop="novelname">
              <el-input id="novelname" type="text" v-model="novelForm.novelname" auto-complete="off" placeholder="请输入文章名称"></el-input>
            </el-form-item>
            <el-form-item label="文章简介" prop="novelbrief">
              <el-input id="novelbrief" type="text" v-model="novelForm.novelbrief" auto-complete="off" placeholder="请输入文章简介"></el-input>
            </el-form-item>
            <el-form-item label="作者" prop="author">
              <el-input id="author" type="text" v-model="novelForm.author" auto-complete="off" placeholder="请输入作者"></el-input>
            </el-form-item>
            <el-form-item label="内容" prop="content">    
              <quill-editor id="content"
                  v-model="novelForm.content" 
                  ref="myQuillEditor" 
                  :options="editorOption" 
                  @blur="onEditorBlur($event)" 
                  @focus="onEditorFocus($event)"
                  @change="onEditorChange($event)">
              </quill-editor>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="novelEditDialog=false">取 消</el-button>
            <el-button type="primary" @click="saveChanges">确 定</el-button>
          </div>
      </el-dialog>
      <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
                    @current-change="handlePageChange"></el-pagination>
  </div>
</template>

<script>
const api_name='/ap/novel'
export default {
    name: "novellist",
    data() {
    return {
      novels: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      novelInfo: '',
      novelEditDialog: false,
      seanovelname:'',
      seaauthor:'',
      novelForm: {
        id: '',
        novelname: '',
        novelbrief: '',
        author:'',
        content:''
      },rules: {
        novelname: [
          { required: true, message: '请输入文章名称', trigger: 'blur' }
        ],
        novelbrief: [
          { required: true, message: '请输入文章简介', trigger: 'blur' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' }
        ],
      },
      //富文本
      editorOption: {
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'], // 标题插件
            [{ color: [] }, { background: [] }], // 颜色相关插件
            [{ list: 'ordered' }, { list: 'bullet' }], // 列表相关插件
            [{ script: 'sub' }, { script: 'super' }], // 上下标相关插件
            [{ header: [1, 2, 3, 4, 5, false] }], // 标题级别插件
            ['blockquote', 'code-block'], // 引用、代码块插件
            ['formula', 'mathjax'],  // 公式相关插件
            ['clean'], // 清除格式
          ],
        },
      },
    }
  },
  mounted() {
    this.getNovel()
  },
  methods: {
    getNovel() {
      this.$axios( {
        method:"get",
        url: `${api_name}/page`,
        params: {
          page: this.currentPage - 1,
          size: this.pageSize
        }
      }).then(response => {
        this.novels = response.data.data.content
        this.total = response.data.data.totalElements
        console.log(this.novels)
      })
    },
    infoNovel(row){
      this.novelInfo=row.content;
      this.dialogVisible = true;
    },
    editNovel(row) {
      console.log(row);
      this.novelForm.id=row.id;
      this.novelForm.novelname = row.novelname; 
      this.novelForm.novelbrief = row.novelbrief; 
      this.novelForm.author = row.author; 
      this.novelForm.content=row.content;
      console.log(this.novelForm);
      this.novelEditDialog = true// 显示编辑框
    
    },
    novelhandleClose(done) {
      // 保存数据并关闭对话框
        done();
     
    },
    deleteNovel(novel) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios({
            method:"delete",
            url: `${api_name}/deletenovel`,
            data: {
              id:novel.id
            }
          }).then(res => {
            console.log(res)
            if(res.data.code==200){
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
              }else {
                this.$message.error('修改失败');
              }
            this.getNovel()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
      
    },
    handlePageChange(currentPage) {
      this.currentPage = currentPage
      this.getNovel()
    },
    saveChanges() {
      // 判断表单数据是否有效
       this.$axios({
          method:"Put",
          url: `${api_name}/updatenovel`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data: this.novelForm
        }).then((res)=>{
          if(res.data.code==200){
            this.$message({
              message: '修改成功',
              type: 'success'
             });     
          }else {
            this.$message.error('修改失败');
          }
          this.novelEditDialog = false;
          this.$refs.novelForm.resetFields();
          this.getNovel()
        }).catch(
            err => console.log(err))
    },
    search() {
      this.$axios({
        method:"post",
        url:`${api_name}/searchnovel`,
        headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
        params: { 
          novelname: this.seanovelname,
          author: this.seaauthor,
          page: this.currentPage - 1,
          size: this.pageSize
        } 
      }).then((response) => {
        console.log(response)
        this.novels = response.data.data.content
        this.total = response.data.data.totalElements
      })
    },
  }
}
</script>

<style>
.el-dialog{
  width: 90%;
}
</style>