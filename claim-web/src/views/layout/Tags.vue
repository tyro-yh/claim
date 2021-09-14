<template>
	<el-row>
		<el-col :span="20">
			<el-tag
			  v-for="(tag,index) in tagList"
			  :key="index+''"
			  :effect="effect(tag)"
			  :closable="tag.meta.closable"
			  @close="delTags(tag)"
			  @click="changeTags(tag)">
			  {{tag.meta.title}} <span v-if="tag.params">{{tag.params.reportNo}}</span>
			</el-tag>
		</el-col>
		<el-col :span="4">
			<div class="tags-close-box">
			    <el-dropdown>
			        <el-button size="mini" type="primary">
			            标签选项
			            <i class="el-icon-arrow-down el-icon--right"></i>
			        </el-button>
			        <template #dropdown>
			            <el-dropdown-menu size="small">
			                <el-dropdown-item @click.native="clossOther">关闭其他</el-dropdown-item>
			                <el-dropdown-item @click.native="clossAll">关闭所有</el-dropdown-item>
			            </el-dropdown-menu>
			        </template>
			    </el-dropdown>
			</div>
		</el-col>
	</el-row>
</template>

<script>
	export default {
		data() {
			return {
			}
		},
		computed: {
			tagList() {
				return this.$store.state.tagList
			}
		},
		methods: {
			delTags(tag) {
				let nextTag = {};
				this.tagList.forEach((item, index) => {
					if (item.name === tag.name) {
						nextTag = this.tagList[index + 1] || this.tagList[index - 1]
					}
				});
				this.$router.push(nextTag.path);
				this.$store.commit("delTags",tag);
			},
			changeTags(tag) {
				this.$router.push({name:tag.name,params:tag.params});
			},
			clossOther() {
				let thisTag = {};
				this.tagList.forEach((item, index) => {
					if (item.name === this.$route.name) {
						thisTag = item
					}
				});
				this.$store.commit("clossOther",thisTag);
			},
			clossAll() {
				if(this.$route.name != 'Main') {
					this.$router.push({name: 'Main'});
				}
				this.$store.commit("clossAll");
			},
			effect(tag) {
				return this.$route.path == tag.path ? 'dark' : 'plain';
			}
		},
		mounted() {
		}
	}
</script>

<style>
	.el-tag {
		cursor: pointer;
		margin-left: 10px;
	}
	
	.tags-close-box {
		float: right;
	    text-align: center;
	    width: 110px;
	    height: 40px;
		line-height: 40px;
		padding-right: 35px;
	}
</style>
