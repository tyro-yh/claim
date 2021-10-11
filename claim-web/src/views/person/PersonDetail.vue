<template>
  <el-container>
    <el-aside width="150px">
    	<el-col>
    		<el-card shadow="hover" style="margin-top: 20px;">
    			<el-link :underline="false" href="#card1">人员基本信息</el-link>
    		</el-card>
        <el-card shadow="hover" style="margin-top: 2px;">
        	<el-link :underline="false" href="#card2">伤情信息</el-link>
        </el-card>
        <el-card shadow="hover" style="margin-top: 2px;">
        	<el-link :underline="false" href="#card3">跟踪记录</el-link>
        </el-card>
        <el-card shadow="hover" style="margin-top: 2px;">
        	<el-link :underline="false" href="#card4">费用明细情况</el-link>
        </el-card>
    	</el-col>
    </el-aside>
    <el-main>
      <el-col :span="21">
        <el-form ref="form" :rules="rules" :model="form" label-width="100px">
          <el-collapse v-model="activeNames" style="border: none;">
            <el-card id="card1" shadow="hover">
              <el-collapse-item name="1">
                <template slot="title">
                	<span style="font-size: 16px;font-weight: 700;color: #303133;">人员基本信息</span>
                </template>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="是否标的人" prop="personFlag">
                      <el-radio-group v-model="form.personFlag" :disabled="personStatus">
                        <el-radio label="1">是</el-radio>
                        <el-radio label="0">否</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                	<el-col :span="8">
                		<el-form-item label="人伤类型" prop="lossType">
                      <el-select style="width: 100%;" v-model="form.lossType" placeholder="请选择" :disabled="personStatus">
                        <el-option label="门诊" value="1"></el-option>
                        <el-option label="住院" value="2"></el-option>
                        <el-option label="伤残" value="3"></el-option>
                        <el-option label="身故" value="4"></el-option>
                        <el-option label="重疾" value="5"></el-option>
                        <el-option label="津贴" value="6"></el-option>
                        <el-option label="生育" value="7"></el-option>
                      </el-select>
                		</el-form-item>
                	</el-col>
                	<el-col :span="8">
                		<el-form-item label="人员名称" prop="personName">
                			<el-input v-model="form.personName" :disabled="personStatus"></el-input>
                		</el-form-item>
                	</el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="证件类型" prop="identifyType">
                      <el-select style="width: 100%;" v-model="form.identifyType" placeholder="请选择" @change="getSexAndAge" :disabled="personStatus">
                        <el-option label="身份证" value="01"></el-option>
                        <el-option label="户口薄" value="02"></el-option>
                        <el-option label="护照" value="03"></el-option>
                        <el-option label="军官证" value="04"></el-option>
                        <el-option label="驾驶证" value="05"></el-option>
                        <el-option label="其他" value="99"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                  	<el-form-item label="证件号码" prop="identifyNo">
                  		<el-input v-model="form.identifyNo" @change="getSexAndAge" :disabled="personStatus"></el-input>
                  	</el-form-item>
                  </el-col>
                  <el-col :span="8">
                  	<el-form-item label="联系电话" prop="phone">
                  		<el-input v-model="form.phone" :disabled="personStatus"></el-input>
                  	</el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="性别">
                      <el-select style="width: 100%;" v-model="form.sex" placeholder="请选择" :disabled="personStatus">
                        <el-option label="男" value="1"></el-option>
                        <el-option label="女" value="2"></el-option>
                        <el-option label="未知" value="0"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                  	<el-form-item label="年龄">
                      <el-input-number style="width: 100%;" v-model="form.age" :precision="0" :min="0" label="年龄" :controls="false" :disabled="personStatus"></el-input-number>
                  	</el-form-item>
                  </el-col>
                  <el-col :span="8">
                  	<el-form-item label="出生日期">
                      <el-date-picker
                        style="width: 100%;"
                        format="yyyy-MM-dd"
                        v-model="form.birthday"
                        type="date"
                        placeholder="选择日期" :disabled="personStatus">
                      </el-date-picker>
                  	</el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="16">
                  	<el-form-item label="跟踪反馈">
                      <el-input
                        type="textarea"
                        :rows="2"
                        placeholder="请输入内容"
                        maxlength="500"
                        show-word-limit
                        v-model="form.content" :disabled="personStatus">
                      </el-input>
                  	</el-form-item>
                  </el-col>
                </el-row>
              </el-collapse-item>
            </el-card>

            <el-card id="card2" shadow="hover" style="margin-top: 20px;">
              <el-collapse-item name="2">
                <template slot="title">
                	<span style="font-size: 16px;font-weight: 700;color: #303133;">伤情信息</span>
                </template>
                <el-table :data="injuredList" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
                  <el-table-column label="受伤部位" min-width="180">
                  	<template slot-scope="scope">
                      <el-select v-model="scope.row.injuredPart" :disabled="personStatus">
                        <el-option v-for="item in injuredPartList"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value">
                        </el-option>
                      </el-select>
                  	</template>
                  </el-table-column>
                  <el-table-column label="伤情诊断" min-width="180">
                  	<template slot-scope="scope">
                      <el-autocomplete
                        popper-class="my-autocomplete"
                        style="width: 100%;"
                        v-model="scope.row.diagnosisName"
                        :fetch-suggestions="queryDiagnosis"
                        placeholder="支持模糊搜索"
                        @select="((item) => {selectDiagnosis(item, scope.row)})" :disabled="personStatus">
                      </el-autocomplete>
                  	</template>
                  </el-table-column>
                  <el-table-column label="治疗方式" width="200">
                  	<template slot-scope="scope">
                      <el-select v-model="scope.row.treatment" :disabled="personStatus">
                        <el-option label="保守治疗" value="0"></el-option>
                        <el-option label="手术治疗" value="1"></el-option>
                      </el-select>
                  	</template>
                  </el-table-column>
                  <el-table-column label="治疗方案" width="200">
                  	<template slot-scope="scope">
                      <el-select v-model="scope.row.treatWay" :disabled="personStatus">
                        <el-option v-for="item in treatWayList"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value">
                        </el-option>
                      </el-select>
                  	</template>
                  </el-table-column>
                  <el-table-column label="具体诊断" width="200">
                  	<template slot-scope="scope">
                      <el-input v-model="scope.row.specificDiagnosis" :disabled="personStatus"></el-input>
                  	</template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                  	<template slot-scope="scope">
                  		<el-button size="small" icon="el-icon-delete" type="danger" @click="delInjured(scope.$index)" :disabled="personStatus"></el-button>
                  	</template>
                  </el-table-column>
                </el-table>
                <el-col :span="24" class="toolbar" style="background: #fff;">
                	<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addInjured" :disabled="personStatus"></el-button>
                </el-col>
              </el-collapse-item>
            </el-card>

            <el-card id="card3" shadow="hover" style="margin-top: 20px;">
              <el-collapse-item name="3">
                <template slot="title">
                	<span style="font-size: 16px;font-weight: 700;color: #303133;">跟踪记录</span>
                </template>
                <el-table :data="traceList" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
                  <el-table-column label="跟踪日期" min-width="180">
                  	<template slot-scope="scope">
                      <el-date-picker
                        style="width: 100%;"
                        format="yyyy-MM-dd"
                        v-model="scope.row.traceTime"
                        type="date"
                        placeholder="请输入跟踪日期" :disabled="personStatus">
                      </el-date-picker>
                  	</template>
                  </el-table-column>
                  <el-table-column label="目前身体状况" min-width="180">
                  	<template slot-scope="scope">
                  		<el-input v-model="scope.row.bodyStatus" :disabled="personStatus"></el-input>
                  	</template>
                  </el-table-column>
                  <el-table-column label="缴费方式" width="140">
                  	<template slot-scope="scope">
                  	  <el-select v-model="scope.row.payWay" :disabled="personStatus">
                  	    <el-option label="医保" value="0"></el-option>
                  	    <el-option label="自费" value="1"></el-option>
                  	    <el-option label="公费" value="2"></el-option>
                  	  </el-select>
                  	</template>
                  </el-table-column>
                  <el-table-column label="已发生医疗费用" width="140">
                  	<template slot-scope="scope">
                  		<el-input-number style="width: 100%;" v-model="scope.row.incurredFee" :precision="2" :min="0" :controls="false" :disabled="personStatus"></el-input-number>
                  	</template>
                  </el-table-column>
                  <el-table-column label="估计还需医疗费用" width="140">
                  	<template slot-scope="scope">
                  		<el-input-number style="width: 100%;" v-model="scope.row.requiredFee" :precision="2" :min="0" :controls="false" :disabled="personStatus"></el-input-number>
                  	</template>
                  </el-table-column>
                  <el-table-column label="后续治疗内容" width="200">
                  	<template slot-scope="scope">
                  		<el-input v-model="scope.row.treamentContent" :disabled="personStatus"></el-input>
                  	</template>
                  </el-table-column>
                  <el-table-column label="跟踪人员" width="180">
                  	<template slot-scope="scope">
                      <el-autocomplete
                        popper-class="my-autocomplete"
                        style="width: 100%;"
                        v-model="scope.row.tracePersonName"
                        :fetch-suggestions="queryHandler"
                        placeholder="支持模糊搜索"
                        @select="((item) => {selectHandler(item, scope.row)})" :disabled="personStatus">
                        <template slot-scope="{ item }">
                          <span style="float: left">{{ item.value }}</span>
                          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
                        </template>
                      </el-autocomplete>
                  	</template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                  	<template slot-scope="scope">
                  		<el-button size="small" icon="el-icon-delete" type="danger" @click="delTrace(scope.$index)" :disabled="personStatus"></el-button>
                  	</template>
                  </el-table-column>
                </el-table>
                <el-col :span="24" class="toolbar" style="background: #fff;">
                	<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addTrace" :disabled="personStatus"></el-button>
                </el-col>
              </el-collapse-item>
            </el-card>

            <el-card id="card4" shadow="hover" style="margin-top: 20px; margin-bottom: 60px;">
              <el-collapse-item name="4">
                <template slot="title">
                	<span style="font-size: 16px;font-weight: 700;color: #303133;">费用明细情况</span>
                </template>
                <el-row :gutter="20" v-loading="loading">
                  <el-col :span="8">
                  	<el-form-item label="合计金额">
                  		<el-input v-model="sumAmt" disabled></el-input>
                  	</el-form-item>
                  </el-col>
                </el-row>
                <el-table :data="feeList" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
                  <el-table-column label="费用名目">
                  	<template slot-scope="scope">
                      <el-select v-model="scope.row.feeTypeCode" @change="((value) => {selectFeeType(value,scope.row)})" :disabled="personStatus">
                        <el-option v-for="item in feeTypeList"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value">
                        </el-option>
                      </el-select>
                  	</template>
                  </el-table-column>
                  <el-table-column label="索赔金额">
                    <template slot-scope="scope">
                    	<el-input-number style="width: 100%;" @change="getEstimateLoss(scope.row)" v-model="scope.row.claimLoss" :precision="2" :min="0" :controls="false" :disabled="personStatus"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column label="扣减金额">
                    <template slot-scope="scope">
                    	<el-input-number style="width: 100%;" @change="getEstimateLoss(scope.row)" v-model="scope.row.deductionFee" :precision="2" :min="0" :controls="false" :disabled="personStatus"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column label="实际金额">
                    <template slot-scope="scope">
                    	<el-input-number style="width: 100%;" disabled v-model="scope.row.estimateLoss" :precision="2" :min="0" :controls="false"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                  	<template slot-scope="scope">
                  		<el-button size="small" icon="el-icon-delete" type="danger" @click="delFee(scope.$index)" :disabled="personStatus"></el-button>
                  	</template>
                  </el-table-column>
                </el-table>
                <el-col :span="24" class="toolbar" style="background: #fff;">
                	<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addFee" :disabled="personStatus"></el-button>
                </el-col>
              </el-collapse-item>
            </el-card>
          </el-collapse>
        </el-form>
      </el-col>
      <el-row style="position: fixed;bottom: 10px;margin-left: 30%;">
      	<el-button type="primary" @click="handleSave" :disabled="personStatus">保存</el-button>
      	<el-button type="primary" @click="handleSubmit" :disabled="personStatus">保存并提交</el-button>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
  import { getDiagnosisList,initPersonInfo,savePerson,submitPerson,getHandlerList } from '@/api/api';
  import { formatTimeToStr} from '@/common/js/date.js';
  export default {
  	props: ['reportNo','personId'],
  	data() {
  		return {
  			loading: false,
        personStatus: false,
  			activeNames: ['1','2','3','4'],
  			total: 0,
  			page: 1,
  			pageSize: 5,
  			form: {},
        injuredList: [],
        traceList: [],
        feeList: [],
        injuredPartList: [
          {label:"头部", value:"1"},
          {label:"面部", value:"2"},
          {label:"颈部", value:"3"},
          {label:"胸部", value:"4"},
          {label:"腹部", value:"5"},
          {label:"背部", value:"6"},
          {label:"脊柱", value:"7"},
          {label:"上肢", value:"8"},
          {label:"手", value:"9"},
          {label:"下肢", value:"10"},
          {label:"足", value:"11"},
          {label:"全身", value:"12"},
          {label:"其他", value:"13"}
        ],
        treatWayList: [
          {label:"清创缝合" ,value:"1"},
          {label:"骨折内固定" ,value:"2"},
          {label:"植骨" ,value:"3"},
          {label:"断肢再植术" ,value:"4"},
          {label:"血管神经吻合" ,value:"5"},
          {label:"截肢" ,value:"6"},
          {label:"开颅手术" ,value:"7"},
          {label:"脏器修补术" ,value:"8"},
          {label:"器官摘除术" ,value:"9"},
          {label:"其他" ,value:"10"}
        ],
        feeTypeList: [
          {label:"医药费" ,value:"1"},
          {label:"后续治疗费" ,value:"2"},
          {label:"伙食补助费" ,value:"3"},
          {label:"整容费" ,value:"4"},
          {label:"营养费" ,value:"5"},
          {label:"其他" ,value:"6"},
        ],
        rules: {
          personFlag: [
            { required: true, message: '请选择是否标的人', trigger: 'blur' }
          ],
          lossType: [
            { required: true, message: '请选择人伤类型', trigger: 'blur' }
          ],
          personName: [
            { required: true, message: '请输入人员名称', trigger: 'blur' }
          ],
          identifyType: [
            { required: true, message: '请选择证件类型', trigger: 'blur' }
          ],
          identifyNo: [
            { required: true, message: '请输入证件号码', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' }
          ],
        }
  		}
  	},
    computed: {
      sumAmt() {
        let sum = 0;
        this.feeList.forEach(item => {
          if(item && item.estimateLoss) {
            sum = sum + item.estimateLoss
          }
        })
        return sum;
      }
    },
  	methods: {
  		cellClass() {
  			return 'text-align: center;'
  		},
  		headCellClass() {
  			return 'background-color: #f5f7fa !important;text-align: center;'
  		},
      addInjured() {
      	this.injuredList.push({
      		reportNo: this.reportNo
      	})
      },
      addTrace() {
      	this.traceList.push({
      		reportNo: this.reportNo
      	})
      },
      addFee() {
        this.feeList.push({
        	reportNo: this.reportNo
        })
      },
      delInjured(index) {
      	this.$confirm('是否删除该伤情?', '提示', {
      		confirmButtonText: '确定',
      		cancelButtonText: '取消',
      		type: 'warning'
      	}).then(() => {
      		this.injuredList.splice(index,1);
      	}).catch(() => {
      		this.$message({
      			type: 'info',
      			message: '已取消删除'
      		});
      	});
      },
      delTrace(index) {
      	this.$confirm('是否删除该记录?', '提示', {
      		confirmButtonText: '确定',
      		cancelButtonText: '取消',
      		type: 'warning'
      	}).then(() => {
      		this.traceList.splice(index,1);
      	}).catch(() => {
      		this.$message({
      			type: 'info',
      			message: '已取消删除'
      		});
      	});
      },
      delFee(index) {
      	this.$confirm('是否删除该记录?', '提示', {
      		confirmButtonText: '确定',
      		cancelButtonText: '取消',
      		type: 'warning'
      	}).then(() => {
      		this.feeList.splice(index,1);
      	}).catch(() => {
      		this.$message({
      			type: 'info',
      			message: '已取消删除'
      		});
      	});
      },
      queryDiagnosis(queryString, cb) {
        let params = {
          queryString: queryString
        }
        getDiagnosisList(params).then(res => {
          let restaurants = res.data.data;
          cb(restaurants);
        })
      },
      selectDiagnosis(item,row) {
      	row.diagnosisCode = item.label;
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
      selectHandler(item,row) {
      	row.tracePersonCode = item.label;
      },
      selectFeeType(value,row) {
        if(this.feeList.filter(item => item.feeTypeCode == value).length > 1) {
          this.$message.warning("该费用名目已存在不能重复选择");
          row.feeTypeCode = '';
        }
      },
      getEstimateLoss(row) {
        if(!isNaN(row.claimLoss) && !isNaN(row.deductionFee)) {
          row.estimateLoss = row.claimLoss - row.deductionFee;
        }
      },
      getSexAndAge() {
        let identifyType = this.form.identifyType;
        let identifyNo = this.form.identifyNo;
        if(identifyType && identifyNo && identifyType == '01' && identifyNo.length >= '16') {
          //获取年龄
          let myDate = new Date();
          let month = myDate.getMonth() + 1;
          let day = myDate.getDate();
          let age = myDate.getFullYear() - identifyNo.substring(6, 10) - 1;
          let birthday = identifyNo.substring(6, 10) + "-" + identifyNo.substring(10, 12) + "-" + identifyNo.substring(12, 14);
          this.form.birthday = birthday;
          if (identifyNo.substring(10, 12) < month || identifyNo.substring(10, 12) == month && identifyNo.substring(12, 14) <= day) {
              age++;
          }
          this.form.age = age;
          //获取性别
          if (parseInt(identifyNo.substr(16, 1)) % 2 == 1) {
              //男
              this.form.sex = '1';
          } else {
              //女
              this.form.sex = '2';
          }
        }
      },
      getPerson() {
        let params = {
        	reportNo: this.reportNo,
        	personId: this.personId
        };
        this.loading = true;
        initPersonInfo(params).then((res) => {
        	this.form = res.data.data.personMain;
          if(res.data.data.injuredList) {
          	this.injuredList = res.data.data.injuredList;
          }
          if(res.data.data.traceList) {
          	this.traceList = res.data.data.traceList;
          }
          if(res.data.data.feeList) {
          	this.feeList = res.data.data.feeList;
          }
          if('0' != this.form.underwriteFlag) {
          	this.personStatus = true;
          }
        	setTimeout(() => {
        		this.loading = false;
        	},500);
        });
      },
      handleSave() {
        this.form.sumAmt = this.sumAmt;
        this.$refs['form'].validate((valid) => {
          if (valid) {
            if(this.checkRules()) {
              let params = {
              	personMain: this.form,
              	injuredList: this.injuredList,
              	traceList: this.traceList,
              	feeList: this.feeList
              }
              savePerson(params).then((res) => {
              	if(res) {
              		this.$message.success("保存成功");
              		this.personId = res.data.data.personMain.id;
              		this.getPerson();
              	}
              });
            } else {
              return false;
            }
          } else {
            this.$message.warning("校验不通过");
            return false;
          }
        });
      },
      handleSubmit() {
        this.form.sumAmt = this.sumAmt;
        this.$refs['form'].validate((valid) => {
          if (valid) {
            if(this.checkRules()) {
              let params = {
              	personMain: this.form,
              	injuredList: this.injuredList,
              	traceList: this.traceList,
              	feeList: this.feeList
              }
              submitPerson(params).then((res) => {
              	if(res) {
              		this.$message.success("提交成功");
              		this.personId = res.data.data.personMain.id;
              		this.getPerson();
              	}
              });
            } else {
              return false;
            }
          } else {
            this.$message.warning("校验不通过");
            return false;
          }
        });
      },
      checkRules() {
        let flag = true;
        if(!this.traceList || this.traceList.length <= 0) {
          this.$message.warning("跟踪记录不能为空");
          return false;
        }

        if(!this.feeList || this.feeList.length <= 0) {
          this.$message.warning("费用明细不能为空");
          return false;
        }

        this.traceList.forEach(item => {
          if(!item.traceTime) {
            this.$message.warning("跟踪日期不能为空");
            flag = false;
            return false;
          }
          if(isNaN(item.incurredFee)) {
            this.$message.warning("已发生医疗费用不能为空");
            flag = false;
            return false;
          }
          if(!item.tracePersonCode) {
            this.$message.warning("跟踪人员不能为空");
            flag = false;
            return false;
          }
        })

        if(!flag) {
          return flag;
        }

        this.feeList.forEach(item => {
          if(!item.feeTypeCode) {
            this.$message.warning("费用名目不能为空");
            flag = false;
            return false;
          }
          if(isNaN(item.claimLoss)) {
            this.$message.warning("索赔金额不能为空");
            flag = false;
            return false;
          }
          if(isNaN(item.deductionFee)) {
            this.$message.warning("扣减金额不能为空");
            flag = false;
            return false;
          }
        })

        if(!flag) {
          return flag;
        }

        if(this.injuredList && this.injuredList.length > 0) {
          this.injuredList.forEach(item => {
            if(!item.injuredPart) {
              this.$message.warning("受伤部位不能为空");
              flag = false;
              return false;
            }
            if(!item.diagnosisName) {
              this.$message.warning("伤情诊断不能为空");
              flag = false;
              return false;
            }
          })
        }

        return flag;
      }
  	},
  	mounted() {
      this.getPerson();
  	}
  }
</script>

<style>
</style>
