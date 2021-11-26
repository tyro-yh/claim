<template>
  <section>
    <el-card shadow="hover" style="margin-bottom: 20px;width: 30%;">
      <el-button type="primary" @click="handleAdd">新增人员</el-button>
    </el-card>

    <template v-for="(item,index) in tableData">
      <el-col :span="8" style="margin-top: 20px;">
        <el-card style="height:150px;width:70%;margin-left: 15%;">
          <div style="height: 70px;border-bottom: 2px solid #ccc;">
            <div style="font-size: 22px;">{{item.userName}}</div>
            <div style="font-size: 14px;color: #999;">({{item.userCode}})</div>
          </div>
          <div style="margin-top: 10px;">
            <el-button size="small" type="primary" @click="handleEdit(item)">编辑岗位</el-button>
            <el-button size="small" type="danger" @click="handleDel(item)">删除配置</el-button>
          </div>
        </el-card>
      </el-col>
    </template>

    <!--新增界面-->
    <el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false" :before-close="handleClose">
      <el-form label-position="right" :model="addForm" ref="addForm">
        <el-form-item>
          <el-row type="flex" :gutter="10" justify="center">
            <el-col :span="4">
              <el-button type="primary" @click="addGrage">增加岗位</el-button>
            </el-col>
            <el-col :span="8">
              <el-form-item label="员工名称" label-width="80px">
                <el-autocomplete popper-class="my-autocomplete" style="width: 100%;" v-model="addForm.userName"
                  :fetch-suggestions="queryHandler" placeholder="支持模糊搜索"
                  @select="((item) => {selectHandler(item, addForm)})">
                  <template slot-scope="{ item }">
                    <span style="float: left">{{ item.value }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
                  </template>
                </el-autocomplete>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="员工号" label-width="80px">
                <el-input v-model="addForm.userCode" disabled placeholder="员工号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        <el-table :data="addForm.userGradeList" highlight-current-row style="width: 100%;" border
          :cell-style="cellClass" :header-cell-style="headCellClass">
          <el-table-column label="岗位">
            <template slot-scope="scope">
              <el-autocomplete
                popper-class="my-autocomplete"
                style="width: 100%;"
                v-model="scope.row.gradeCName"
                :fetch-suggestions="queryGrade"
                placeholder="岗位"
                @select="((item) => {selectGrade(item, scope.row)})">
              </el-autocomplete>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button size="small" icon="el-icon-delete" type="danger" @click="delGrade(index)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
        <el-button @click.native="handleClose">取消</el-button>
      </div>
    </el-dialog>

    <!--编辑界面-->
    <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false" :before-close="handleClose2">
      <el-form label-position="right" :model="editForm" ref="editForm">
        <el-form-item>
          <el-row type="flex" :gutter="10" justify="center">
            <el-col :span="4">
              <el-button type="primary" @click="addGrage2">增加岗位</el-button>
            </el-col>
            <el-col :span="8">
              <el-form-item label="员工名称" label-width="80px">
                <el-input v-model="editForm.userName" disabled placeholder="员工号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="员工号" label-width="80px">
                <el-input v-model="editForm.userCode" disabled placeholder="员工号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        <el-table :data="editForm.userGradeList" highlight-current-row style="width: 100%;" border
          :cell-style="cellClass" :header-cell-style="headCellClass">
          <el-table-column label="岗位">
            <template slot-scope="scope">
              <el-autocomplete
                popper-class="my-autocomplete"
                style="width: 100%;"
                v-model="scope.row.gradeCName"
                :fetch-suggestions="queryGrade"
                placeholder="岗位"
                @select="((item) => {selectGrade(item, scope.row)})">
              </el-autocomplete>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button size="small" icon="el-icon-delete" type="danger" @click="delGrade(index)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click.native="addEdit" :loading="editLoading">提交</el-button>
        <el-button @click.native="handleClose2">取消</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
  import { getHandlerList,getGradeList,addUserGrade,getUserGradeList,delUserGrade,initUserGrade,editUserGrade } from '@/api/api';
  import { formatTimeToStr} from '@/common/js/date.js';
  export default {
    data() {
      return {
        addFormVisible: false,
        addForm: {
          userGradeList: []
        },
        editFormVisible: false,
        editForm: {
          userGradeList: []
        },
        total: 0,
        page: 1,
        pageSize: 5,
        tableData: [],
        filters: {},
        gradeList: [],
        addLoading: false,
        editLoading: false,
        listLoading: false
      }
    },
    methods: {
      cellClass() {
        return 'text-align: center;'
      },
      headCellClass() {
        return 'background-color: #f5f7fa !important;text-align: center;'
      },
      formatCreateTime: function (row, column) {
      	return formatTimeToStr(row.createTime, 'yyyy-MM-dd');
      },
      formatUpdateTime: function (row, column) {
      	return formatTimeToStr(row.updateTime, 'yyyy-MM-dd');
      },
      handleSizeChange(val) {
      	this.pageSize = val;
      	this.getUserGrade();
      },
      handleCurrentChange(val) {
      	this.page = val;
      	this.getUserGrade();
      },
      getUserGrade() {
        let params = {
        	page: this.page,
        	pageSize: this.pageSize,
        	name: this.filters.name,
        	code: this.filters.code
        };
        this.listLoading = true;
        getUserGradeList(params).then(res => {
          this.tableData = res.data.records;
          this.total = res.data.total;
          setTimeout(() => {
            this.listLoading = false;
          }, 500);
        })
      },
      handleClose() {
      	this.addForm = {
          userGradeList: []
        };
      	this.addFormVisible = false;
      },
      handleClose2() {
      	this.editForm = {
          userGradeList: []
        };
      	this.editFormVisible = false;
      },
      handleAdd() {
        this.addForm = {
          userGradeList: []
        };
        this.addFormVisible = true;
      },
      handleEdit(item) {
        let params = {
        	userCode: item.userCode
        };
        initUserGrade(params).then(res => {
          this.editForm.userCode = item.userCode;
          this.editForm.userName = item.userName;
          this.editForm.userGradeList = res.data.data;
          this.editFormVisible = true;
        })
      },
      handleDel(item) {
        this.$confirm('是否删除该人员?', '提示', {
        	confirmButtonText: '确定',
        	cancelButtonText: '取消',
        	type: 'warning'
        }).then(() => {
        	let params = {
        	  userCode: item.userCode
        	}
        	delUserGrade(params).then(res => {
            this.$message.success('删除成功');
        	  this.getUserGrade();
        	})
        }).catch(() => {
        	this.$message({
        		type: 'info',
        		message: '已取消删除'
        	});
        });
      },
      queryGrade(queryString, cb) {
        let params = {
          queryString: queryString
        }
        getGradeList(params).then(res => {
          let restaurants = res.data.data;
          cb(restaurants);
        })
      },
      selectGrade(item, row) {
        row.gradeEName = item.label;
      },
      queryHandler(queryString, cb) {
        let params = {
          queryString: queryString
        }
        getHandlerList(params).then(res => {
          let restaurants = res.data.data;
          cb(restaurants);
        })
      },
      selectHandler(item, form) {
        form.userCode = item.label;
      },
      addGrage() {
        if(!this.addForm.userCode || !this.addForm.userName) {
          this.$message.warning('员工名和称员工号不能为空');
          return;
        }
        if(!this.addForm.userGradeList) {
          this.addForm = {
            userGradeList: []
          };
        }
        this.addForm.userGradeList.push({
          userCode: this.addForm.userCode
        })
      },
      addGrage2() {
        if(!this.editForm.userGradeList) {
          this.editForm = {
            userGradeList: []
          };
        }
        this.editForm.userGradeList.push({
          userCode: this.editForm.userCode
        })
      },
      delGrade(index) {
        this.$confirm('是否删除该岗位?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.addForm.userGradeList.splice(index, 1);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      delGrade2(index) {
        this.$confirm('是否删除该岗位?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.editForm.userGradeList.splice(index, 1);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      addSubmit() {
        if(!this.addForm.userCode || !this.addForm.userName) {
          this.$message.warning('员工名和称员工号不能为空');
          return;
        }
        let flag = true;
        this.addForm.userGradeList.forEach(item => {
          if(!item.gradeEName) {
            flag = false;
            return false;
          }
        });
        if(!flag) {
          this.$message.warning('岗位不能为空');
          return;
        }
        this.addLoading = true;
        let params = this.addForm;
        addUserGrade(params).then(res => {
          setTimeout(() => {
            this.addLoading = false;
          }, 500);
          if (res.data.data.status == '0') {
            this.$message.warning(res.data.data.msg);
          } else {
            this.$message.success("保存成功");
            this.handleClose();
            this.getUserGrade();
          }
        })
      },
      addEdit() {
        let flag = true;
        this.editForm.userGradeList.forEach(item => {
          if(!item.gradeEName) {
            flag = false;
            return false;
          }
        });
        if(!flag) {
          this.$message.warning('岗位不能为空');
          return;
        }
        this.editLoading = true;
        let params = this.editForm;
        editUserGrade(params).then(res => {
          setTimeout(() => {
            this.editLoading = false;
          }, 500);
          if (res.data.data.status == '0') {
            this.$message.warning(res.data.data.msg);
          } else {
            this.$message.success("保存成功");
            this.handleClose2();
            this.getUserGrade();
          }
        })
      }
    },
    mounted() {
      this.getUserGrade();
    }
  }
</script>

<style>
</style>
