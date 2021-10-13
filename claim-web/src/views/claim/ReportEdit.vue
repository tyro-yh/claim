<template>
	<section>
		<el-card shadow="hover">
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item label="保单号">
						<el-input v-model="filters.policyNo" placeholder="请输入保单号"></el-input>
					</el-form-item>
					<el-form-item label="投保人证件">
						<el-input v-model="filters.appliCode"></el-input>
					</el-form-item>
					<el-form-item label="被保险人证件">
						<el-input v-model="filters.insuredCode"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" v-on:click="getPolicy">查询</el-button>
					</el-form-item>
				</el-form>
			</el-col>
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="listLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="policyNo" label="保单号" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="insuranceName" label="险类" min-width="120">
				</el-table-column>
				<el-table-column prop="insuredName" label="被保险人名称" min-width="100">
				</el-table-column>
				<el-table-column prop="insuredCode" label="被保险人证件" min-width="120">
				</el-table-column>
				<el-table-column prop="startTime" label="起保日期" min-width="120" :formatter="formatStartTime">
				</el-table-column>
				<el-table-column prop="endTime" label="终保日期" min-width="120" :formatter="formatEndTime">
				</el-table-column>
				<el-table-column prop="policyStatus" label="保单状态" width="80" :formatter="formatStatus">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" v-if="scope.row.policyStatus == '1'" @click="selectPolicy(scope.$index, scope.row)">选择</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>

		<el-card shadow="hover" style="margin-top: 20px;margin-bottom: 20px;" v-show="showReport">
			<div slot="header" class="clearfix">
			    <span style="font-size: 22px;">报案基本信息</span>
				<!-- <div style="float: right; padding: 3px 0">
					<el-button  type="text">提交</el-button>
				</div> -->
      </div>
			<el-form v-if="!form.reportNo" ref="form" :rules="rules" :model="form" style="margin-left:20%;width:60%;min-width:600px;">
				<el-form-item>
					<el-col :span="3">报案号</el-col>
					<el-col :span="9">
						<el-input v-model="form.reportNo" disabled placeholder="提交自动生成"></el-input>
					</el-col>
					<el-col :span="3">报案时间</el-col>
					<el-col :span="9">
						<el-input v-model="form.reportTime" disabled placeholder="提交自动生成"></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">保单号</el-col>
					<el-col :span="9">
						<el-input v-model="form.policyNo" disabled></el-input>
					</el-col>
					<el-col :span="3">险种</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuranceName" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">被保险人名称</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuredName" disabled></el-input>
					</el-col>
					<el-col :span="3">被保险人证件</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuredCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">归属机构</el-col>
					<el-col :span="9">
						<el-input v-model="form.comName" disabled></el-input>
					</el-col>
					<el-col :span="3">保单类型</el-col>
					<el-col :span="9">
						<el-radio v-model="form.policyType" disabled label="01">个人</el-radio>
						<el-radio v-model="form.policyType" disabled label="02">团体</el-radio>
					</el-col>
				</el-form-item>
        <el-form-item>
          <el-col :span="3"><span style="color: #F56C6C;margin-right: 4px;">*</span>出险原因</el-col>
          <el-col :span="9">
            <el-form-item prop="damageCode">
              <el-select style="width: 100%;" v-model="form.damageCode" placeholder="请选择出险原因">
                <el-option
                  v-for="item in damageOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="3"><span style="color: #F56C6C;margin-right: 4px;">*</span>出险时间</el-col>
          <el-col :span="9">
            <el-form-item prop="damageTime">
              <el-date-picker
                style="width: 100%;"
                format="yyyy-MM-dd HH"
                v-model="form.damageTime"
                type="datetime"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
				<el-form-item>
					<el-col :span="3">出险地点</el-col>
					<el-col :span="3" style="display: flex;">
						<el-autocomplete
						  popper-class="my-autocomplete"
						  style="width: 100%;"
						  v-model="provinceName"
						  :fetch-suggestions="queryProvince"
						  placeholder="省"
						  @select="selectProvince">
						</el-autocomplete>
					</el-col>
					<el-col v-show="provinceName" :span="1">-</el-col>
					<el-col v-show="provinceName" :span="3">
						<el-autocomplete
						  popper-class="my-autocomplete"
						  style="width: 100%;"
						  v-model="cityName"
						  :fetch-suggestions="queryCity"
						  placeholder="市"
						  @select="selectCity">
						</el-autocomplete>
					</el-col>
					<el-col v-show="cityName" :span="1">-</el-col>
					<el-col v-show="cityName" :span="3">
						<el-autocomplete
						  style="width: 100%;"
						  v-model="countyCodeName"
						  :fetch-suggestions="queryCountyCode"
						  placeholder="区"
						  @select="selectCountyCode"
						></el-autocomplete>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="9">
						<!-- <el-autocomplete
						style="width: 100%;"
						  v-model="form.damageAddress"
						  :fetch-suggestions="queryAddress"
						  placeholder="详细地址"
						  @select="handleDamageCode"
						></el-autocomplete> -->
						<el-input v-model="form.damageAddress" maxlength="200" placeholder="详细地址"></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">报损金额</el-col>
					<el-col :span="9" style="display: flex;">
						<el-input-number v-model="form.reportLoss" :precision="2" :min="0" label="报损金额"></el-input-number>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3"><span style="color: #F56C6C;margin-right: 4px;">*</span>报案人</el-col>
					<el-col :span="9">
            <el-form-item prop="reportorName">
              <el-input v-model="form.reportorName"></el-input>
            </el-form-item>
					</el-col>
					<el-col :span="3"><span style="color: #F56C6C;margin-right: 4px;">*</span>报案人电话</el-col>
					<el-col :span="9">
            <el-form-item prop="reportorPhone">
              <el-input v-model="form.reportorPhone"></el-input>
            </el-form-item>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">联系人</el-col>
					<el-col :span="9">
						<el-input v-model="form.linkerName"></el-input>
					</el-col>
					<el-col :span="3">联系人电话</el-col>
					<el-col :span="9">
						<el-input v-model="form.linkerPhone"></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险经过描述</el-col>
					<el-col :span="21">
						<el-input type="textarea" v-model="form.remark" maxlength="500" show-word-limit></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click='handleSubmit'>提交</el-button>
					<el-button @click.native='handleResat'>重置</el-button>
				</el-form-item>
			</el-form>

      <el-result v-if="form.reportNo" icon="success" title="报案成功">
        <template slot="subTitle">
          报案号:{{form.reportNo}}
        </template>
        <template slot="extra">
          <el-button type="primary" size="medium" @click="toWorkflow">流程图</el-button>
        </template>
      </el-result>

		</el-card>
	</section>
</template>

<script>
	import { getPolicyList,getDamageCodes,getProvinceList,getCityList,getCountyCodeList,saveReport } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		data() {
			return {
				filters: {
					policyNo: '',
					insuredCode: '',
					appliCode: ''
				},
				total: 0,
				page: 1,
				pageSize: 5,
				listLoading: false,
				showReport: false,
				provinceName: '',
				cityName: '',
				countyCodeName: '',
				form: {
					reportNo: '',
					reportTime: '',
					policyNo: '',
					insuranceName: '',
					insuranceCode: '',
					insuredName: '',
					insuredCode: '',
					policyType: '',
					comCode: '',
					comName: '',
					reportLoss: 0,
					damageCode: '',
					damageTime: '',
					province: '',
					city: '',
					countyCode: '',
					damageAddress: '',
					reportorName: '',
					reportorPhone: '',
					linkerName: '',
					linkerPhone: '',
					remark: ''
				},
        rules: {
            damageCode: [
              { required: true, message: '请选择出险原因', trigger: 'change' }
            ],
            damageTime: [
              { type: 'date', required: true, message: '请选择出险时间', trigger: 'change' }
            ],
            reportorName: [
              { required: true, message: '请输入报案人', trigger: 'blur' }
            ],
            reportorPhone: [
              { required: true, message: '请输入报案人电话', trigger: 'blur' }
            ],
        },
				damageOptions: [],
				tableData: []
			}
		},
		methods: {
			//有效标志显示转换
			formatStatus(row, column) {
				let status = '';
				switch(row.policyStatus) {
					case '0' : status = '未生效';break;
					case '1' : status = '已生效';break;
					case '2' : status = '已过期';break;
					case '9' : status = '已退保';break;
					default: '未知'
				}
				return status;
			},
			formatStartTime(row, column) {
				return formatTimeToStr(row.startTime, 'yyyy-MM-dd hh:mm:ss');
			},
			formatEndTime(row, column) {
				return formatTimeToStr(row.endTime, 'yyyy-MM-dd hh:mm:ss');
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getCommon();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getCommon();
			},
			selectPolicy(index, row) {
				this.form.policyNo = row.policyNo;
				this.form.insuranceName = row.insuranceName;
				this.form.insuranceCode = row.insuranceCode;
				this.form.insuredName = row.insuredName;
				this.form.insuredCode = row.insuredCode;
				this.form.policyType = row.policyType;
				this.form.comCode = row.comCode;
				this.form.comName = row.comName;
				this.showReport = true;
			},
			//获取字典列表
			getPolicy() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					policyNo: this.filters.policyNo,
					insuredCode: this.filters.insuredCode,
					appliCode: this.filters.appliCode
				};
				this.listLoading = true;
				//NProgress.start();
				getPolicyList(params).then((res) => {
					this.tableData = res.data.records;
					this.total = res.data.total;
					this.listLoading = false;
					//NProgress.done();
				});
			},
			queryProvince(queryString, cb) {
        let params = {
          queryString: queryString
        }
        getProvinceList(params).then(res => {
          let restaurants = res.data.data;
          cb(restaurants);
        })
			},
			queryCity(queryString, cb) {
       let params = {
        	preCode: this.form.province,
          queryString: queryString
        };
        getCityList(params).then((res) => {
          let restaurants = res.data.data;
          cb(restaurants);
        });
			},
			queryCountyCode(queryString, cb) {
        let params = {
        	preCode: this.form.city,
          queryString: queryString
        };
        getCountyCodeList(params).then((res) => {
        	let restaurants = res.data.data;
        	cb(restaurants);
        });
			},
			selectProvince(item) {
				this.form.province = item.label;
				this.provinceName = item.value;
			},
			selectCity(item) {
				this.form.city = item.label;
				this.cityName = item.value;
			},
			selectCountyCode(item) {
				this.form.countyCode = item.label;
				this.countyCodeName = item.value;
			},
			getDamageOptions() {
				getDamageCodes().then((res) => {
					this.damageOptions = res.data.data;
				});
			},
			cellClass() {
				return 'text-align: center;'
			},
			headCellClass() {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			handleSubmit() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            saveReport(this.form).then((res) => {
              this.$message.success("报案成功");
              this.form = res.data.data
            });
          } else {
            return false;
          }
        });
			},
      handleResat() {
        this.$refs['form'].resetFields();
      },
      toWorkflow() {
        let routeUrl = this.$router.resolve({
        	name: 'Workflow',
        	params: {
        		reportNo:this.form.reportNo
        	}
        });
        window.open(routeUrl.href, '_blank');
      }
		},
		mounted() {
			this.getDamageOptions();
		}
	}
</script>

<style>
	.labelTd {
		width: 100px;
		text-align: right;
	}

	.el-autocomplete-suggestion {
		width: 160px !important;
	}
</style>
