<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/teacher/welcome' }" @click.native="saveNavState('/teacher/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>选课管理</el-breadcrumb-item>
			  <el-breadcrumb-item>选课信息管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br/>
			<el-card>			
				<el-row :gutter="20">
					<el-col :span="4">
						<el-select v-model="queryInfo.kechengleibie"  placeholder="请选择课程类别">
							<el-option
							v-for="item in leibie"
							:key="item.type"
							:label="item.type"
							:value="item.type"></el-option>
						</el-select>
					</el-col>
					<el-col :span="6">
						<el-input placeholder="请输入查找内容" clearable v-model="queryInfo.query" @clear="clearValues">
							<el-button slot="append" icon="el-icon-search" @click="getSelectionListLike()"></el-button>
						</el-input>
					</el-col>
				</el-row>
				<!-- 选课信息列表区域 -->
				<el-table :data="selectionList" ref="table" boder stripe :height="tableHeight">
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="kechengid" label="课程ID"></el-table-column>
					<el-table-column align="center" prop="kechengname" label="课程名称"></el-table-column>
					<el-table-column align="center" prop="kechengleibie" label="课程类别"></el-table-column>
					<el-table-column align="center" prop="count" label="已选人数"></el-table-column>
					<el-table-column align="center" label="操作" width="300px">
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-s-fold" size="mini" @click="showDetailsDialog(scope.row.kechengid)">课程详情</el-button>
							<el-button type="primary" icon="el-icon-s-unfold" size="mini" @click="showSelectDialog(scope.row.kechengid)" :disabled="scope.row.count == '0'">选课详情</el-button>
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
						<el-form-item label="开课时间" prop="ctime">
							<el-input disabled v-model="detailsForm.ctime"></el-input>
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
					<el-button type="primary" @click="goCurri">前往修改</el-button>
				</span>
			</el-dialog>
			<!-- 选课详情对话框:显示当前课程的所有选课学生信息 -->
			<el-dialog center title="选课详情" :visible.sync="selectDialogVisible" width="90%">
				<el-table :data="selectList" ref="table" border stripe >
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="xuehao" label="学号"></el-table-column>
					<el-table-column align="center" prop="name" label="姓名"></el-table-column>
					<el-table-column align="center" prop="sex" label="性别"></el-table-column>
					<el-table-column align="center" prop="college" label="学院"></el-table-column>
					<el-table-column align="center" prop="major" label="专业"></el-table-column>
					<el-table-column align="center" prop="banji" label="班级"></el-table-column>
					<el-table-column align="center" prop="ctime" label="选课时间"></el-table-column>
				</el-table>
				<span slot="footer" class="dialog-footer">
				  <el-button @click="selectDialogVisible = false">取 消</el-button>				 
				</span>
			</el-dialog>
		</div>	
	</el-container>
</template>

<script>
	export default {
		name: 'CourseSelection',
		data() {
			return{
				tableHeight: 100,
				queryInfo: {
					query: '',
					kechengleibie: ''
				},
				selectionList: [],
				totle: 0,
				leibie: [{
					"id": 0,
					"type": '',
				},
				{
					"id": 1,
					"type": "必修"
				},
				{
					"id": 2,
					"type": "选修"
				}],
				detailsForm: {},
				detailsDialogVisible: false,
				selectList: [],
				selectDialogVisible: false,
			}
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
		created() {
			this.saveNavState('/teacher/course')
			this.queryInfo.query =''
			this.getSelectionList()
		},
		methods: {
			// 跳转到修改课程信息界面		
			goCurri() {			
				window.sessionStorage.setItem('tag','0')
				window.sessionStorage.setItem('classid',this.detailsForm.kechengid.toString())
				window.sessionStorage.setItem('activePath','/teacher/curriculum')			
				this.detailsDialogVisible = false							
				this.$router.push('/teacher/curriculum')
				this.$router.go(0)
				
			},
			// 课程详情对话框
			showDetailsDialog(kechengid) {
				this.$axios.get('teacher/getcurri/' + kechengid)
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
			// 选课详情对话框
			showSelectDialog(kechengid) {
				this.$axios.get('teacher/getallselection/' + kechengid).
				then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取课程详情信息失败!")
					}
					else {
						this.selectList = res.data
						this.selectDialogVisible = true
					}				
				})
			},
			// 对课程名称进行模糊查询
			getSelectionListLike() {			
				this.getSelectionList()
			},
			// 清除数据
			clearValues() {
				this.queryInfo.query = ''
				this.queryInfo.kechengleibie = ''
				this.getSelectionList()
			},
			// 获取选课情况信息列表
			getSelectionList() {
				this.$axios.post('teacher/getselection',{
					kechengteacher: window.sessionStorage.getItem("username"),
					kechengname: this.queryInfo.query,
					kechengleibie: this.queryInfo.kechengleibie,
				})
				.then(res => {			
					if(res.status !== 200) {
						return this.$message.error("获取选课信息列表失败！")
					}	
					this.selectionList = res.data
					this.totle = res.data.length
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
