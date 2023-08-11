<template>
    <div class="index">
        <el-form :model="novelForm" label-width="80px">
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
        <el-form-item>
            <el-button type="primary" @click="addNovle">添加文章</el-button>
        </el-form-item>
        </el-form>
    </div>
</template>

<script>

import { ElEditor } from 'element-ui';
const api_name='/ap/novel'
export default {
    components: {
      ElEditor, // 注册 el-editor 组件
    },
    name: "addnovel",
    data(){
    return {
       novelForm:{
        novelname:'',
        novelbrief:'',
        author:'',
        content:''
       },
      content: '',
      checked:true,
      rules: {
        novelname: [{required: true, message: '请输入文章名称', trigger: 'blur'}],
        novelbrief: [{required: true, message: '请输入文章简介', trigger: 'blur'}],
        author: [{required: true, message: '请输入作者', trigger: 'blur'}],
      },
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
  },
  methods: {
    handleChange(value) {
      this.content = value
    },
    addNovle() {
      let novel = this.novelForm//将数据写入变量
        console.log(novel)
        this.$axios({
          method:"post",
          url: `${api_name}/insertNovel`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data: novel
        }).then((res)=>{
          
          if(res.data.code==200){
            this.$message({
              message: '新增成功',
              type: 'success'
             });
             
          }
          else {
            this.$message.error('新增失败');
          }
          this.novelForm={}
          // this.resetForm('loginform')
        }).catch(
        err => console.log(err))
    }
   
  }
}
</script>

<style>

</style>