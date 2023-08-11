<template>
    <div class="userindex">
        <el-table :data="novels" stripe>
        <el-table-column prop="novelname" label="文章名称"></el-table-column>
        <el-table-column prop="novelbrief" label="文章简介"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="opentime" label="发布时间"></el-table-column>
        <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="info" icon="el-icon-view"  @click="infoNovel(scope.row)" circle></el-button>
            </template>
        </el-table-column>
        </el-table>
        <el-dialog title="内容" :visible.sync="dialogVisible"  :close-on-click-modal ="false">
          <div v-html="novelInfo"></div>
        </el-dialog>
        <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
                    @current-change="handlePageChange"></el-pagination>
    </div>
</template>

<script>
const api_name='/ap/novel'
export default {
    name: "novel",
    data() {
        return {
            novels: [],
            currentPage: 1,
            pageSize: 10,
            total: 0,
            novelInfo: '',
            dialogVisible: false,
        }
    },
  mounted() {
    // 在组件挂载时从后端获取多个信息
    this.$axios({
        method:"get",
        url: `${api_name}/page`,
        params: {
          page: this.currentPage - 1,
          size: this.pageSize
        }
      }).then(response => {
        this.novels = response.data.content
        this.total = response.data.totalElements
        console.log(this.novels)
      })
  },
  methods:{
    infoNovel(row){
      this.novelInfo=row.content;
      this.dialogVisible = true;
    },
    handleClose(done) {
      // 保存数据并关闭对话框
        done();
     
    },
    handlePageChange(currentPage) {
      this.currentPage = currentPage
      this.getBook()
    },
  }
}
</script>

<style>

</style>