<template>
  <el-container>
    <el-aside width="150px">
    	<el-col>
    		<el-card shadow="hover" style="margin-top: 20px;">
    			<el-link :underline="false" href="#card1">保单基本信息</el-link>
    		</el-card>
    		<el-card shadow="hover" style="margin-top: 2px;">
    			<el-link :underline="false" href="#card2">报案基本信息</el-link>
    		</el-card>
    		<el-card shadow="hover" style="margin-top: 2px;">
    			<el-link :underline="false" href="#card3">人员基本信息</el-link>
    		</el-card>
        <el-card shadow="hover" style="margin-top: 2px;">
        	<el-link :underline="false" href="#card4">伤情信息</el-link>
        </el-card>
        <el-card shadow="hover" style="margin-top: 2px;">
        	<el-link :underline="false" href="#card5">费用信息</el-link>
        </el-card>
    		<el-card shadow="hover" style="margin-top: 2px;">
    			<el-link :underline="false" href="#card6">审核信息</el-link>
    		</el-card>
    	</el-col>
    </el-aside>
    <el-main>
      <el-col :span="21">
        <el-collapse v-model="activeNames" style="border: none;">
          <el-card id="card1" shadow="hover">
          	<el-collapse-item name="1">
          		<template slot="title">
          			<span style="font-size: 16px;font-weight: 700;color: #303133;">保单基本信息</span>
          		</template>
          		<BasePolicy :reportNo="reportNo"/>
          	</el-collapse-item>
          </el-card>
          <el-card id="card2" shadow="hover" style="margin-top: 20px;">
          	<el-collapse-item name="2">
          		<template slot="title">
          			<span style="font-size: 16px;font-weight: 700;color: #303133;">报案基本信息</span>
          		</template>
          		<BaseReport :reportNo="reportNo"/>
          	</el-collapse-item>
          </el-card>
          <el-card id="card3" shadow="hover" style="margin-top: 20px;">
            <el-form label-width="100px">
            <el-collapse-item name="3">
              <template slot="title">
              	<span style="font-size: 16px;font-weight: 700;color: #303133;">人员基本信息</span>
              </template>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="是否标的人">
                    <el-radio-group v-model="personMain.personFlag" disabled>
                      <el-radio label="1">是</el-radio>
                      <el-radio label="0">否</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              	<el-col :span="8">
              		<el-form-item label="人伤类型">
                    <el-select style="width: 100%;" v-model="personMain.lossType" placeholder="请选择" disabled>
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
              		<el-form-item label="人员名称">
              			<el-input v-model="personMain.personName" disabled></el-input>
              		</el-form-item>
              	</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="证件类型">
                    <el-select style="width: 100%;" v-model="personMain.identifyType" placeholder="请选择" disabled>
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
                	<el-form-item label="证件号码">
                		<el-input v-model="personMain.identifyNo" disabled></el-input>
                	</el-form-item>
                </el-col>
                <el-col :span="8">
                	<el-form-item label="联系电话">
                		<el-input v-model="personMain.phone" disabled></el-input>
                	</el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="性别">
                    <el-select style="width: 100%;" v-model="personMain.sex" placeholder="请选择" disabled>
                      <el-option label="男" value="1"></el-option>
                      <el-option label="女" value="2"></el-option>
                      <el-option label="未知" value="0"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                	<el-form-item label="年龄">
                    <el-input-number style="width: 100%;" v-model="personMain.age" :precision="0" :min="0" label="年龄" :controls="false" disabled></el-input-number>
                	</el-form-item>
                </el-col>
                <el-col :span="8">
                	<el-form-item label="出生日期">
                    <el-date-picker
                      style="width: 100%;"
                      format="yyyy-MM-dd"
                      v-model="personMain.birthday"
                      type="date"
                      placeholder="选择日期" disabled>
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
                      v-model="personMain.content" disabled>
                    </el-input>
                	</el-form-item>
                </el-col>
              </el-row>
            </el-collapse-item>
            </el-form>
          </el-card>
          <el-card id="card4" shadow="hover" style="margin-top: 20px;">
            <el-collapse-item name="4">
              <template slot="title">
              	<span style="font-size: 16px;font-weight: 700;color: #303133;">伤情信息</span>
              </template>
              <el-table :data="injuredList" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
                <el-table-column label="受伤部位" min-width="180">
                	<template slot-scope="scope">
                    <el-select v-model="scope.row.injuredPart" disabled>
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
                    <el-input v-model="scope.row.diagnosisName" disabled></el-input>
                	</template>
                </el-table-column>
                <el-table-column label="治疗方式" width="200">
                	<template slot-scope="scope">
                    <el-select v-model="scope.row.treatment" disabled>
                      <el-option label="保守治疗" value="0"></el-option>
                      <el-option label="手术治疗" value="1"></el-option>
                    </el-select>
                	</template>
                </el-table-column>
                <el-table-column label="治疗方案" width="200">
                	<template slot-scope="scope">
                    <el-select v-model="scope.row.treatWay" disabled>
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
                    <el-input v-model="scope.row.specificDiagnosis" disabled></el-input>
                	</template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
          </el-card>
          <el-card id="card5" shadow="hover" style="margin-top: 20px;">
            <el-collapse-item name="5">
              <template slot="title">
              	<span style="font-size: 16px;font-weight: 700;color: #303133;">费用明细情况</span>
              </template>
              <el-row :gutter="20" v-loading="loading" style="line-height: 40px;">
                <el-col :span="2">合计金额</el-col>
                <el-col :span="4">
                	<el-input v-model="personMain.sumAmt" disabled></el-input>
                </el-col>
              </el-row>
              <el-table :data="feeList" v-loading="loading" highlight-current-row style="width: 100%;margin-top: 10px;" border :cell-style="cellClass" :header-cell-style="headCellClass">
                <el-table-column label="费用名目">
                	<template slot-scope="scope">
                    <el-select v-model="scope.row.feeTypeCode" disabled>
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
                  	<el-input-number style="width: 100%;" disabled v-model="scope.row.claimLoss" :precision="2" :min="0" :controls="false"></el-input-number>
                  </template>
                </el-table-column>
                <el-table-column label="扣减金额">
                  <template slot-scope="scope">
                  	<el-input-number style="width: 100%;" disabled v-model="scope.row.deductionFee" :precision="2" :min="0" :controls="false"></el-input-number>
                  </template>
                </el-table-column>
                <el-table-column label="实际金额">
                  <template slot-scope="scope">
                  	<el-input-number style="width: 100%;" disabled v-model="scope.row.estimateLoss" :precision="2" :min="0" :controls="false"></el-input-number>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
          </el-card>
          <el-card id="card6" shadow="hover" style="margin-top: 20px; margin-bottom: 60px;">
          	<el-collapse-item name="6">
          		<template slot="title">
          			<span style="font-size: 16px;font-weight: 700;color: #303133;">审核信息</span>
          		</template>
          		<BaseApprove :businessKey="businessKey" :handler="handler"/>
          	</el-collapse-item>
          </el-card>
        </el-collapse>
      </el-col>
    </el-main>
  </el-container>
</template>

<script>
  import BasePolicy from '@/views/claim/BasePolicy.vue';
  import BaseReport from '@/views/claim/BaseReport.vue';
  import BaseApprove from '@/views/claim/BaseApprove.vue';
  import { initPersonApproveInfo } from '@/api/api';
  import { formatTimeToStr} from '@/common/js/date.js';
  export default {
  	props: ['businessKey','reportNo','handler'],
  	components: {
      BasePolicy,
      BaseReport,
  		BaseApprove
  	},
  	data() {
  		return {
        loading: false,
  			activeNames: ['1','3','4','5','6'],
  			endCaseInfo: {},
        personMain : {},
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
        ]
  		}
  	},
  	methods: {
      cellClass() {
      	return 'text-align: center;'
      },
      headCellClass() {
      	return 'background-color: #f5f7fa !important;text-align: center;'
      },
      getPersonApproveInfo() {
        let params = {
        	reportNo: this.reportNo,
        	businessKey: this.businessKey
        }
        this.loading = true;
        initPersonApproveInfo(params).then(res => {
          this.personMain = res.data.data.personMain;
          if(res.data.data.injuredList) {
            this.injuredList = res.data.data.injuredList;
          }
          if(res.data.data.traceList) {
            this.traceList = res.data.data.traceList;
          }
          if(res.data.data.feeList) {
            this.feeList = res.data.data.feeList;
          }
          setTimeout(() => {
          	this.loading = false;
          },500);
        });
      }
  	},
  	mounted() {
      this.getPersonApproveInfo();
  	}
  }
</script>

<style>
</style>
