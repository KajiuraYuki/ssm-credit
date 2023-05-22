<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/student/welcome' }" @click.native="saveNavState('/student/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>选课中心</el-breadcrumb-item>
			  <el-breadcrumb-item>我的选课</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20" id="row1">
					<!-- 模糊查询课程名称 -->
					<el-col :span="6">
						<el-input placeholder="请输入查询内容" clearable v-model="queryInfo.like"  @clear="getCurriSelfList">
							<el-button slot="append" icon="el-icon-search" @click="getCurriSelfList"></el-button>
						</el-input>
					</el-col>
					<!-- 选择课程类别  选修/必修 -->
					<el-col :span="4">
						<el-select v-model="queryInfo.kechengleibie" placeholder="请选择课程类别">
							<el-option
							v-for="item in leibie"
							:key="item.type"
							:label="item.type"
							:value="item.type"
							>
							</el-option>
						</el-select>
					</el-col>
					<el-col :span="3">
						<el-select v-model="queryInfo.xuefen" placeholder="请选择课程学分">
							<el-option
								v-for="item in KCxuefen"
								:key="item.type"
								:label="item.type"
								:value="item.type"></el-option>
						</el-select>
					</el-col>
					<!-- 搜索按钮 -->
					<el-col :span="2">
						<el-button type="primary" @click="getCurriSelfList">搜索</el-button>
					</el-col>
					<!-- 清空搜索按钮 -->
					<el-col :span="3">
						<el-button type="info" @click="clearValues">清除搜索</el-button>
					</el-col>
				</el-row>
				
				<!-- 我的选课信息列表 -->
				<el-table :data="selectionSelfList" boder ref="table" stripe :height="tableHeight">
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="kechengid" label="课程ID"></el-table-column>
					<el-table-column align="center" prop="kechengname" label="课程名称"></el-table-column>
					<el-table-column align="center" prop="kechengleibie" label="课程类别"></el-table-column>
					<el-table-column align="center" prop="name" label="开课教师"></el-table-column>
					<el-table-column align="center" prop="xuefen" label="课程学分"></el-table-column>
					<el-table-column align="center" prop="ctime" label="选课时间" width="170px"></el-table-column><strong></strong>
					<el-table-column align="center" label="操作" width="250px">
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showDetailsDialog(scope.row.kechengid)">课程详情</el-button>
							<el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteSelection(scope.row.id)">退课</el-button>
						</template>			
					</el-table-column>
				</el-table>
				<!-- 信息条数提示 -->
				<el-pagination
				layout="total"
				:total="totle"></el-pagination>
			</el-card>
			<!-- 课程详情对话框 -->
			<el-dialog center title="课程详情" :visible.sync="detailsDialogVisible" width="40%">
				<el-form
					:model="detailsForm"				
					label-width="80px">
						<el-form-item label="课程ID" prop="kechengid">
							<el-input disabled v-model="detailsForm.kechengid"></el-input>
						</el-form-item>
						<el-form-item label="课程名称" prop="kechengname">
							<el-input disabled v-model="detailsForm.kechengname"></el-input>
						</el-form-item>
						<el-form-item label="课程类别" prop="kechengleibie">
							<el-input disabled v-model="detailsForm.kechengleibie"></el-input>
						</el-form-item>
						<el-form-item label="课程学分" prop="xuefen">
							<el-input disabled v-model="detailsForm.xuefen"></el-input>
						</el-form-item>
						<el-form-item label="上课教室" prop="classroom">
							<el-input disabled v-model="detailsForm.classroom"></el-input>
						</el-form-item>
						<el-form-item label="开始周次" prop="startweek">
							<el-input disabled v-model="detailsForm.startweek"></el-input>
						</el-form-item>
						<el-form-item label="结束周次" prop="endweek">
							<el-input disabled v-model="detailsForm.endweek"></el-input>
						</el-form-item>
						<el-form-item label="上课周次" prop="classweek">
							<el-input disabled v-model="detailsForm.classweek"></el-input>
						</el-form-item>
						<el-form-item label="上课节次" prop="classsection">
							<el-input disabled v-model="detailsForm.classsection"></el-input>
						</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="detailsDialogVisible = false">取消</el-button>					
				</span>
			</el-dialog>
		</div>
	</el-container>
</template>

<script>
	export default {
		name: "StuSelectCurriSelf",
		data() {
			return {
				tableHeight: 100,
				queryInfo: {
					like: '',
					kechengleibie: '',
					xuefen: '',
					xuehao: window.sessionStorage.getItem("username")
				},
				leibie: [{
					"id": 1,
					"type": "必修"
				},
				{
					"id": 2,
					"type": "选修"
				}],
				KCxuefen: [{
					"id": 1,
					"type": "1"
				},{
					"id": 2,
					"type": "2"
				},{
					"id": 3,
					"type": "3"
				},{
					"id": 4,
					"type": "4"
				},{
					"id": 5,
					"type": "5"
				}],			
				selectionSelfList: [],
				totle: 0,
				detailsDialogVisible: false,
				detailsForm: {}
				
			}
		},
		created() {
			this.saveNavState('/student/courseself')
			this.getCurriSelfList()
		},
		mounted: function() {
			this.$nextTick(function () {
				this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 95;
				let self = this;
				window.onresize = function() {
					self.tableHeight = window.innerHeight - self.$refs.table.$el.offsetTop - 95
				}
			})
		},
		methods: {			
			// 进行查询，获取当前账户所选择的课程信息列表
			getCurriSelfList() {
				this.$axios.post('sutdent/getselectionself',{
					xuehao: this.queryInfo.xuehao,
					like: this.queryInfo.like,
					kechengleibie: this.queryInfo.kechengleibie,
					xuefen: this.queryInfo.xuefen
				})
				.then(res => {
					if(res.status !== 200){
						return this.$message.error("获取选课信息失败！")
					}			
					this.selectionSelfList = res.data
					this.totle = res.data.length
				})
			},
			// 清除搜索按钮的清除事件
			clearValues() {
				this.queryInfo.like = ''
				this.queryInfo.kechengleibie = ''
				this.queryInfo.xuefen = ''
				this.getCurriSelfList()
			},
			// 课程详情对话框
			showDetailsDialog(kechengid) {
				this.$axios.get('student/getcurri/' + kechengid)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取课程信息失败！")
					}
					else {
						this.detailsForm = res.data
						this.detailsDialogVisible = true						
					}
				})				
			},
			// 删除当前选课记录
			async deleteSelection(id) {
				const confirmResult = await this.$confirm('此操作将删除该条选课记录，请确认：是否退课？','提示',{
					$confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).catch(err => err)
				if (confirmResult !== 'confirm') {
					return this.$message.info("已取消退课！")
				}
				this.$axios.delete('student/deleteselection/' + id)
				.then(res => {					
					if (res.status !== 200) {
						return this.$message.error('退课失败!')
					}
					this.$message.success("退课成功！")
					this.getCurriSelfList()
				})
			},
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath',activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			}
		}
	}
</script>

<style scoped="scoped">
	#one {
		width: 100%;
		height: auto;
		padding-top: 1%;
	}
	#two {
		width: 100%;
		height: 100%;
	}
	.el-card {
		height: 100%;
		width: 100%;
	}
	.el-table{
		padding-top: 20px;
	}
	.el-pagination{
		text-align: left;
		padding-top: 20px;
	}
</style>
