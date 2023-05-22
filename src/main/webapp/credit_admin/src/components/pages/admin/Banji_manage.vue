<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/admin/welcome' }" @click.native="saveNavState('/admin/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>综合管理</el-breadcrumb-item>
			  <el-breadcrumb-item>班级管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20">			
					<el-col :span="6">
						<el-input placeholder="请输入查找内容" clearable v-model="queryInfo.query" clearable @clear="getBJList()" >
							<el-button slot="append" icon="el-icon-search" @click="getBJList()"></el-button>
						</el-input>					
					</el-col>					
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="queryInfo.select_college" placeholder="请选择学院" >
					    <el-option
					      v-for="item1 in college"
					      :key="item1"
					      :label="item1"
					      :value="item1"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="queryInfo.select_major" placeholder="请选择专业" >
					    <el-option
					      v-for="item1 in major"
					      :key="item1"
					      :label="item1"
					      :value="item1"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<!-- 搜索按钮 -->
					<el-col :span="2">
					  <el-button type="primary" @click="getBJList">搜索</el-button>
					</el-col>
					<!-- 清空搜索按钮 -->
					<el-col :span="3">
					  <el-button type="info" @click="clearValues">清除搜索</el-button>
					</el-col>
					<!-- 添加班级信息按钮 -->
					<el-col :span="3">
					  <el-button type="primary" @click="addBanJiButton">添加班级</el-button>
					</el-col>
				</el-row>
				<!-- 用户信息列表区域 -->
				<el-table :data="BJList" ref="table" boder stripe :height="tableHeight">
					<!-- stripe  :  斑马条纹
						 border  :  边框
					-->
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="collegename" label="学院"></el-table-column>
					<el-table-column align="center" prop="majorname" label="专业"></el-table-column>					
					<el-table-column align="center" prop="banjiname" label="班级名称"></el-table-column>					
					<el-table-column align="center" prop="ctime" label="创建时间"></el-table-column>
					<el-table-column align="center" label="操作" width="200px">
						<template slot-scope="scope" >
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.banjiid)">编辑</el-button>							
							<el-button type="danger" icon="el-icon-delete" size="mini" @click="removeBJBybanjiid(scope.row.banjiid)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<!-- 信息条数提示 -->
				<el-pagination
				layout="total"
				:total="totle"></el-pagination>
			</el-card>
			<!-- 编辑班级信息对话框 -->
			<el-dialog center title="编辑班级信息" :visible.sync="editBanjiDialogVisible" width="40%" >
			  <el-form
			    :model="editBanjiForm"
			    ref="editBanjiForm"
			    :rules="editBanjiFormRules"
			    label-width="80px">
				<el-form-item disabled label="班级名称" prop="banjiname">
				  <el-input v-model="editBanjiForm.banjiname"></el-input>
				</el-form-item>
			    <el-form-item disabled label="所属学院" prop="collegename">
			      <el-select v-model="editBanjiForm.collegename" @change="collegechange" placeholder="请选择所属学院">
			      		<el-option
							v-for="item in CollegeData"
			      			:key="item.collegename"
			      			:label="item.collegename"
			      			:value="item.collegename">
						</el-option>
			      </el-select>
			    </el-form-item>
				<el-form-item disabled label="所属专业" prop="majorname">
				  <el-select v-model="editBanjiForm.majorname" placeholder="请选择所属专业">
				  	<el-option
						v-for="item in MajorData"
				  		:key="item.majorname"
				  		:label="item.majorname"
				  		:value="item.majorname">
					</el-option>
				  </el-select>
				</el-form-item>	
				<el-form-item label="创建时间" prop="ctime">
				  <el-input disabled v-model="editBanjiForm.ctime"></el-input>
				</el-form-item>	
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="editBanjiForm.content" />
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="editBanjiDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="editBanji">确 定</el-button>
			  </span>
			</el-dialog>
			<!-- 添加班级信息对话框 -->
			<el-dialog center title="添加班级信息" :visible.sync="addBanjiDialogVisible" width="40%" @close="addDialogClosed" >
			  <el-form
			    :model="addBanjiForm"
			    ref="addBanjiForm"
			    :rules="addBanjiFormRules"
			    label-width="80px">
				<el-form-item disabled label="班级名称" prop="banjiname">
				  <el-input v-model="addBanjiForm.banjiname"></el-input>
				</el-form-item>
			    <el-form-item disabled label="所属学院" prop="collegename">
			      <el-select v-model="addBanjiForm.collegename" @change="collegechange" placeholder="请选择所属学院">
			      		<el-option
							v-for="item in CollegeData"
			      			:key="item.collegename"
			      			:label="item.collegename"
			      			:value="item.collegename">
						</el-option>
			      </el-select>
			    </el-form-item>
				<el-form-item disabled label="所属专业" prop="majorname">
				  <el-select v-model="addBanjiForm.majorname" placeholder="请选择所属专业">
				  	<el-option
						v-for="item in MajorData"
				  		:key="item.majorname"
				  		:label="item.majorname"
				  		:value="item.majorname">
					</el-option>
				  </el-select>
				</el-form-item>
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="addBanjiForm.content" />
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="addBanjiDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="addBanJi()">确 定</el-button>
			  </span>
			</el-dialog>
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'Banji_Manage',
		data() {
			return {
				tableHeight: 100,
				queryInfo: {
					query: '',
					select_college: '',
					select_major: '',
				},
				BJList: [],
				college: [],
				major: [],
				totle: 0,
				editBanjiForm: {},
				editBanjiDialogVisible: false,
				editBanjiFormRules: {
					banjiname: [
						{ required: true, message: '班级名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '班级名称长度为 3 ~ 10个字!', trigger: 'change' }				
					],
					collegename: [
						{ required: true, message: '所属学院不能为空!', trigger: 'change' },
					],
					majorname: [
						{ required: true, message: '所属专业不能为空!', trigger: 'change' },
					]
				},
				addBanjiForm: {},
				addBanjiDialogVisible: false,
				addBanjiFormRules: {
					banjiname: [
						{ required: true, message: '班级名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '班级名称长度为 3 ~ 10个字!', trigger: 'change' }				
					],
					collegename: [
						{ required: true, message: '所属学院不能为空!', trigger: 'change' },
					],
					majorname: [
						{ required: true, message: '所属专业不能为空!', trigger: 'change' },
					]
				},
				CollegeData: [],
				MajorData: [],
				BanjiData: []
				
				
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
			this.saveNavState('/admin/banjimanage')
			this.getInfo()
			this.getBJList()
		},
		methods: {
			// 获取 学院 和 专业信息，用于条件筛选查询
			getInfo() {
				// 获取学院信息
				this.$axios.get('teacher/getcollegegroup')
				.then( res => {
					if (res.status !== 200) {
						return this.$message.error("获取学院信息失败！")
					}
					else {
						this.college = res.data	
						//console.log(res.data)
					}
				})
				
				// 获取专业信息
				this.$axios.get('teacher/getmajorgroup')
				.then( res => {
					if (res.status !== 200) {
						return this.$message.error("获取专业信息失败！")
					}
					else {
						this.major = res.data	
					}
				})
			},
			getBJList() {
				// 获取班级信息，并对数据进行筛选
				this.$axios.post('admin/getbjinfo',{
					like: this.queryInfo.query,
					collegename: this.queryInfo.select_college,
					majorname: this.queryInfo.select_major,
				})
				.then(res => {					
					if (res.status !== 200) {
						return this.$message.error('获取班级信息失败！')
					}
					this.BJList = res.data
					this.totle = res.data.length
				})
			},
			// 清除数据
			clearValues() {
				this.queryInfo.query = ''
				this.queryInfo.select_college = ''
				this.queryInfo.select_major = ''
				this.getBJList()
			},
			// 根据班级id删除班级信息
			async removeBJBybanjiid(banjiid) {
				const confirmResult = await this.$confirm('此操作将永久删除该班级信息并删除该班级下的所有学生信息及学生选课信息，是否继续？','提示',{
					confirmButtonText: '确定',
					cancelButtonText:  '取消',
					type: 'warning'
				}).catch(err => err)
				if(confirmResult !== 'confirm') {
					return this.$message.info('已取消删除！')
				}
				this.$axios.delete('admin/deletebanji/' + banjiid)
				.then(res => {
					if(res.status !== 200) {
						return this.$message.error('删除该班级信息失败！')
					}
					this.$message.success('删除该班级信息成功！')
					this.getBJList()
				})
			},
			// 编辑班级信息
			showEditDialog(banjiid) {
				
				this.MajorData = ''
				this.BanjiData = ''
				
				this.$axios.get('admin/getbjbybjid/' + banjiid)
				.then(res => {					
					if (res.status !== 200) {
						return this.$message.error("获取当前班级信息失败！")
					}
					this.editBanjiForm = res.data
					this.editBanjiDialogVisible= true
				})
				
				// 获取学院信息
				this.$axios.get('teacher/getcollege')
				.then(res => {
					if (res.status !== 200)
						return this.$message.error("获取学院信息失败！")
					this.CollegeData = res.data
				})
			},
			// 修改班级信息
			editBanji() {				
				this.$refs.editBanjiForm.validate(async valid => {
					if(valid) {
						this.$axios.put('admin/updatebanji/' + this.editBanjiForm.banjiid,this.editBanjiForm)
						.then(res => {
							if (res.status !== 200) {
								return this.$message.error("更新班级信息失败！")
							}
							this.editBanjiDialogVisible = false
							this.$message.success("更新班级信息成功！")
							this.getBJList()
							this.getInfo()
						})
					}
				})
			},
			// 选择学院信息后触发事件
			collegechange(value) {
				this.$axios.post('teacher/getmajor/' + value)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取专业信息失败！")
					}
					this.MajorData = res.data
					this.editBanjiForm.majorname = ''									
				})
			},	
			// 选择专业信息后触发事件
			majorchange(value) {
				this.$axios.post('teacher/getbanji/' + value)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取班级信息失败!")
					}
					this.BanjiData = res.data
				})
			},
			// 添加班级 按钮事件
			addBanJiButton() {
				
				this.MajorData = ''
				this.BanjiData = ''
				
				this.addBanjiDialogVisible = true
				// 获取学院信息
				this.$axios.get('teacher/getcollege')
				.then(res => {
					if (res.status !== 200)
						return this.$message.error("获取学院信息失败！")
					this.CollegeData = res.data
				})						
			},
			// 添加班级信息
			addBanJi() {
				this.$refs.addBanjiForm.validate(async valid => {
					if (valid) {
						this.$axios.post('admin/addbanji',this.addBanjiForm)
						.then(res => {
							//console.log(res)
							if (res.status !== 200) {
								return this.$message.error("添加班级信息失败！")
							}
							if (res.status === 200) {
								this.addBanjiDialogVisible = false
								this.$message.success("添加班级信息成功！")							
								this.getBJList()
								this.getInfo()
							}									
						})
					}
				})	
			},
			// 监听 添加班级信息 对话框关闭事件
			addDialogClosed() {
				//取消表验证
				this.$refs['addBanjiForm'].resetFields();				
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
	.el-pagination {
		text-align: left;
		padding-top: 20px;
	}
	#row1 {
		padding-top: 20px;
	}
	
</style>
