<template>
	<section>
		<el-card shadow="hover" style="line-height: 40px;">
			<el-row>
				<el-col :span="1">报案号</el-col>
				<el-col :span="3">
					<el-input size="medium" v-model="reportNo" disabled></el-input>
				</el-col>
			</el-row>
		</el-card>
		<div style="margin-top: 20px;">
      <el-menu default-active="Base" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <template v-for="(item,index) in menuList">
          <el-menu-item :index="item.index">{{item.label}}<i style="font-size: 10px;" v-if="item.close" class="el-icon-close closs-btn" @click="closeMenu(item,index)"></i></el-menu-item>
        </template>
        <el-submenu index="more">
        	<template slot="title">更多</template>
          <template v-for="item in submenuList">
            <el-menu-item :index="item.index">{{item.label}}</el-menu-item>
          </template>
        </el-submenu>
      </el-menu>
			<!-- <el-menu default-active="Base" class="el-menu-demo" mode="horizontal" @select="handleSelect">
				<el-menu-item index="Base">案件基本信息</el-menu-item>
				<el-menu-item index="Claim">立案处理</el-menu-item>
				<el-menu-item index="Settlement">理算处理</el-menu-item>
				<el-menu-item index="DataCollection">资料收集</el-menu-item>
				<el-submenu index="5">
					<template slot="title">更多</template>
					<el-menu-item index="5-1">人伤处理</el-menu-item>
					<el-menu-item index="5-2">查勘处理</el-menu-item>
					<el-menu-item index="5-3">注销与拒赔处理</el-menu-item>
					<el-menu-item index="5-4">案件重开</el-menu-item>
				</el-submenu>
			</el-menu> -->
			<div style="margin-top: 10px;">
				<transition name="fade" mode="out-in" appear>
					<keep-alive>
					    <component :is="activeComponent" :reportNo="reportNo" :caseFlag="caseFlag"/>
					</keep-alive>
				</transition>
			</div>
		</div>
		<div class="navigation">
			<div class="bar">
				<div class="bar-icon">
					<i class="el-icon-s-tools"></i>
				</div>
				<div class="bar-text" align="left">
					<span @click="endCase()">申请结案</span>
				</div>
			</div>
      <div class="bar">
      	<div class="bar-icon">
      		<i class="el-icon-document-delete"></i>
      	</div>
      	<div class="bar-text" align="left">
      		<span @click="cancelCase()">申请注销</span>
      	</div>
      </div>
      <div class="bar">
      	<div class="bar-icon">
      		<i class="el-icon-document-remove"></i>
      	</div>
      	<div class="bar-text" align="left">
      		<span @click="rejectCase()">申请拒赔</span>
      	</div>
      </div>
      <div class="bar">
      	<div class="bar-icon">
      		<i class="el-icon-document-add"></i>
      	</div>
      	<div class="bar-text" align="left">
      		<span @click="reCase()">申请重开</span>
      	</div>
      </div>
		</div>
	</section>
</template>

<script>
	import Base from '@/views/claim/Base.vue'
	import Claim from '@/views/claim/Claim.vue'
	import Settlement from '@/views/claim/Settlement.vue'
	import DataCollection from '@/views/claim/DataCollection.vue'
	import Person from '@/views/person/Person.vue'
	import { getWorkFlowList,applyEndCase,initCaseFlag } from '@/api/api';
	export default {
		props: ['reportNo'],
		mounted(){
      window.addEventListener('storage', event => {
        if(event.key == 'user') {
          let nv = JSON.parse(event.newValue);
          if(nv && (this.sysUserCode != nv.userCode)) {
            window.location.reload();
          }
        }
      })
      let user = localStorage.getItem('user');
      if (user) {
      	user = JSON.parse(user);
      	this.sysUserCode = user.userCode || '';
      }
			this.getCaseFlag();
		},
		components: {
			Base,
			Claim,
			Settlement,
			DataCollection,
      Person
		},
		data() {
		    return {
          sysUserCode: '',
          activeComponent: 'Base',
          collapse: false,
          caseFlag: '',
          menuList: [
            {index: 'Base',label: '案件基本信息'},
            {index: 'Claim',label: '立案处理'},
            {index: 'Settlement',label: '理算处理'},
            {index: 'DataCollection',label: '资料收集'},
          ],
          submenuList: [
            {index: 'Person',label: '人伤处理',close: true},
            {index: 'Claim',label: '查勘处理',close: true},
            {index: 'Settlement',label: '注销与拒赔处理',close: true},
            {index: 'DataCollection',label: '案件重开',close: true},
          ]
		    }
		},
		methods: {
			handleSelect(key, keyPath) {
        if(keyPath.length > 1) {
          let i = '';
          let menu = this.submenuList.forEach((item,index) => {
            if(item.index == key) {
              i = index;
            }
          });
          this.menuList.push(this.submenuList[i]);
          this.submenuList.splice(i,1);
        }
				this.activeComponent = key;
			},
      closeMenu(item,index) {
        this.submenuList.push(item);
        this.menuList.splice(index,1);
      },
			endCase() {
				if(this.caseFlag != '04') {
					this.$confirm('是否申请结案?', '申请结案', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						let params = {
							reportNo: this.reportNo
						}
						applyEndCase(params).then((res) => {
							if(res.data.data.status == '0') {
								this.$message.warning(res.data.data.msg);
							} else {
								this.$message.success('提交成功');
							}
						})
					}).catch(() => {
						this.$message.info('已取消');
					});
				} else {
					this.$message.warning("该案件已结案");
				}
			},
      /* cancelCase() {
        let params = {
        	reportNo: this.reportNo
        }
        applyCancelCase(params).then((res) => {
        	if(res.data.data.status == '0') {
        		this.$message.warning(res.data.data.msg);
        	} else {
        		this.$message.success('提交成功');
        	}
        })
      },
      rejectCase() {
        let params = {
        	reportNo: this.reportNo
        }
        applyRejectCase(params).then((res) => {
        	if(res.data.data.status == '0') {
        		this.$message.warning(res.data.data.msg);
        	} else {
        		this.$message.success('提交成功');
        	}
        })
      },
      reCase() {
        let params = {
        	reportNo: this.reportNo
        }
        applyReCase(params).then((res) => {
        	if(res.data.data.status == '0') {
        		this.$message.warning(res.data.data.msg);
        	} else {
        		this.$message.success('提交成功');
        	}
        })
      }, */
			getCaseFlag() {
				let params = {
					reportNo: this.reportNo
				}
				initCaseFlag(params).then((res) => {
					this.caseFlag = res.data.data;
				})
			}
		}
	}
</script>

<style>
	@import url("../../assets/css/navigation.css");
  .closs-btn:hover {
    color:#20A0FF !important;
  }
</style>
