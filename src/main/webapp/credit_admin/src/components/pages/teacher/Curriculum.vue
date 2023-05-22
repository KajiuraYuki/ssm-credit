<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/teacher/welcome' }" @click.native="saveNavState('/teacher/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>课程管理</el-breadcrumb-item>
			  <el-breadcrumb-item>课程信息管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20" id="row1">
					<!-- 模糊查询课程名称 -->
					<el-col :span="6">
						<el-input placeholder="请输入课程名称" clearable v-model="queryInfo.query"  @clear="getCurriListLike">
							<el-button slot="append" icon="el-icon-search" @click="getCurriListLike"></el-button>
						</el-input>
					</el-col>
					<!-- 选择课程类别  选修/必修 -->
					<el-col :span="4">
						<el-select v-model="queryInfo.select_leibie" placeholder="请选择课程类别">
							<el-option
							v-for="item in leibie"
							:key="item.type"
							:label="item.type"
							:value="item.type"
							>
							</el-option>
						</el-select>
					</el-col>
					<!-- 搜索按钮 -->
					<el-col :span="2">
						<el-button type="primary" @click="getCurriList">搜索</el-button>
					</el-col>
					<!-- 清空搜索按钮 -->
					<el-col :span="3">
						<el-button type="info" @click="clearValues">清除搜索</el-button>
					</el-col>
					<!-- 添加课程信息按钮 -->
					<el-col :span="3">
						<el-button type="primary" @click="newClassBtn">开设课程</el-button>
					</el-col>
				</el-row>
				
				<!-- 课程信息列表 -->
				<el-table :data="curriList" boder ref="table" stripe :height="tableHeight">
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="kechengid" label="课程ID"></el-table-column>
					<el-table-column align="center" prop="kechengname" width="120px" label="课程名称"></el-table-column>
					<el-table-column align="center" prop="kechengleibie" label="课程类别"></el-table-column>
					<el-table-column align="center" prop="xuefen" label="学分"></el-table-column>
					<!-- <el-table-column prop="kechengteacher" label="开课教师ID"></el-table-column> -->
					<el-table-column align="center" prop="classroom" label="上课教室"></el-table-column>
					<el-table-column align="center" prop="startweek" label="开始周次"></el-table-column>
					<el-table-column align="center" prop="endweek" label="结束周次"></el-table-column>
					<el-table-column align="center" prop="classweek" label="上课周次"></el-table-column>
					<el-table-column align="center" prop="classsection" label="上课节次"></el-table-column>
					<el-table-column align="center" label="操作" width="200px">
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.kechengid)">编辑</el-button>
							<el-button type="danger" icon="el-icon-delete" size="mini" @click="removeCurriByKechengid(scope.row.kechengid)">删除</el-button>
						</template>						
					</el-table-column>
				</el-table>
				<!-- 分页区域 -->
				<el-pagination
				  @size-change="handleSizeChange"
				  @current-change="handleCurrentChange"
				  :current-page="queryInfo.pagenum"
				  :page-sizes="[2, 5, 10,15]"
				  :page-size="queryInfo.pagesize"
				  layout="total, sizes, prev, pager, next, jumper"
				  :total="totle"
				></el-pagination>
			</el-card>
			
			<!-- 编辑课程对话框  close-on-click-modal是否允许点击空白处关闭dialog-->
			<el-dialog :close-on-click-modal="false" center title="编辑课程信息" :visible.sync="editCurriDialogVisible" width="40%">
				<el-form
				:model="editCurriForm"
				ref="editCurriForm"
				:rules="editCurriFormRules"
				label-width="80px">
				<el-form-item label="课程ID" prop="kechengid">
					<el-input disabled v-model="editCurriForm.kechengid"></el-input>
				</el-form-item>
				<el-form-item label="课程名称" prop="kechengname">
					<el-input v-model="editCurriForm.kechengname"></el-input>
				</el-form-item>
				<el-form-item label="课程类别" prop="kechengleibie">
					<el-radio v-model="editCurriForm.kechengleibie" label="必修">必修</el-radio>
					<el-radio v-model="editCurriForm.kechengleibie" label="选修">选修</el-radio>
				</el-form-item>
				<el-form-item label="课程学分" prop="xuefen">
					<el-select v-model="editCurriForm.xuefen" placeholder="请选择课程学分">
						<el-option
						v-for="item in KCxuefen"
						:key="item.type"
						:label="item.type"
						:value="item.type">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="开课时间" prop="ctime">
					<el-input disabled v-model="editCurriForm.ctime"></el-input>
				</el-form-item>
				<el-form-item label="可选班级" prop="optionalclass">
					<el-select multiple v-model="Optionalclass" placeholder="请选择可选班级">
						<el-option
						v-for="item in BanjiData"
						:key="item.banjiid"
						:label="item.banjiname"
						:value="item.banjiname">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="上课教室" prop="classroom">
					<el-select v-model="editCurriForm.classroom">
						<el-option
						v-for="item in rooms"
						:key="item"
						:label="item"
						:value="item"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="开始周次" prop="startweek">
					<el-select v-model="editCurriForm.startweek">
						<el-option
						v-for="item in weeks"
						:key="item.week"
						:label="item.week"
						:value="item.week"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="结束周次" prop="endweek">
					<el-select v-model="editCurriForm.endweek">
						<el-option
						v-for="item in weeks"
						:key="item.week"
						:label="item.week"
						:value="item.week"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="上课周次" prop="classweek">
					<el-select v-model="editCurriForm.classweek">
						<el-option
						v-for="item in classweeks"
						:key="item.type"
						:label="item.type"
						:value="item.type"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="上课节次" prop="classsection">
					<el-select v-model="editCurriForm.classsection">
						<el-option
						v-for="item in section"
						:key="item.sec"
						:label="item.sec"
						:value="item.sec"></el-option>
					</el-select>
				</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
				  <el-button @click="quertexitCurri">取 消</el-button>
				  <el-button type="primary" @click="editCurri">确 定</el-button>
				</span>
			</el-dialog>
			<!-- 开设/添加  课程信息 -->
			<el-dialog center title="开设课程" :visible.sync="addCurriDialogVisible" width="40%" @close="addCurriDialogClosed">
				<el-form
					:model="addCurriForm"
					ref="addCurriForm"
					:rules="addCurriFormRules"
					label-width="80px"
				>
					<el-form-item label="课程名称" prop="kechengname">
						<el-input v-model="addCurriForm.kechengname"></el-input>
					</el-form-item>
					<el-form-item label="课程类别" prop="kechengleibie">
						<el-radio v-model="addCurriForm.kechengleibie" label="必修">必修</el-radio>
						<el-radio v-model="addCurriForm.kechengleibie" label="选修">选修</el-radio>
					</el-form-item>
					<el-form-item label="课程学分" prop="xuefen">
						<el-select v-model="addCurriForm.xuefen" placeholder="请选择课程学分">
							<el-option
							v-for="item in KCxuefen"
							:key="item.type"
							:label="item.type"
							:value="item.type">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="可选班级" prop="optionalclass">
						<el-select multiple v-model="Optionalclass" placeholder="请选择可选班级">
							<el-option
							v-for="item in BanjiData"
							:key="item.banjiid"
							:label="item.banjiname"
							:value="item.banjiname">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="上课教室" prop="classroom">
						<el-select v-model="addCurriForm.classroom">
							<el-option
							v-for="item in rooms"
							:key="item"
							:label="item"
							:value="item"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="开始周次" prop="startweek">
						<el-select v-model="addCurriForm.startweek">
							<el-option
							v-for="item in weeks"
							:key="item.week"
							:label="item.week"
							:value="item.week"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="结束周次" prop="endweek">
						<el-select v-model="addCurriForm.endweek">
							<el-option
							v-for="item in weeks"
							:key="item.week"
							:label="item.week"
							:value="item.week"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="上课周次" prop="classweek">
						<el-select v-model="addCurriForm.classweek">
							<el-option
							v-for="item in classweeks"
							:key="item.type"
							:label="item.type"
							:value="item.type"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="上课节次" prop="classsection">
						<el-select v-model="addCurriForm.classsection">
							<el-option
							v-for="item in section"
							:key="item.sec"
							:label="item.sec"
							:value="item.sec"></el-option>
						</el-select>
					</el-form-item>
					</el-form>
					<span slot="footer" class="dialog-footer">
					  <el-button @click="addCurriDialogVisible = false">取 消</el-button>
					  <el-button type="primary" @click="addCurri">确 定</el-button>
					</span>
				</el-form>
			</el-dialog>
			
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'Curriculum',
		data() {		
			// 自定义验证方法   对结束周次进行验证，必须是 结束周次 大于等于 开始周次
			var weekcheckAddend = (rule, value ,callback) => {
				if(value == null) {
					callback(new Error("结束周次不能为空！"))
				}
				if (parseInt(this.addCurriForm.startweek) > parseInt(value)) {
					callback(new Error('结束周次必须大于等于开始周次!'))
				}				
				else{
					callback()
				}
			}
			// 自定义验证方法  对结束周次进行验证，必须是 结束周次 大于等于 开始周次
			var weekcheckend = (rule, value ,callback) => {
				if (parseInt(this.editCurriForm.startweek) > parseInt(value)) {				
					callback(new Error('结束周次必须大于等于开始周次!'))
				}
				else{
					callback()
				}
			}
			
			return {
				tableHeight: 100,
				queryInfo: {
					query: '',
					pagenum: 1,
					pagesize: 5,
					select_leibie: '',
					kechengteacher: window.sessionStorage.getItem("username")
				},
				leibie: [{
					"id": 1,
					"type": "必修"
				},
				{
					"id": 2,
					"type": "选修"
				}],
				classweeks: [{
					"id": 1,
					"type": "周一"
				},{
					"id": 2,
					"type": "周二"
				},{
					"id": 3,
					"type": "周三"
				},{
					"id": 4,
					"type": "周四"
				},{
					"id": 5,
					"type": "周五"
				},{
					"id": 6,
					"type": "周六"
				},{
					"id": 7,
					"type": "周日"
				}],
				weeks: [{
					"id": 1,
					"week": "1"
				},{
					"id": 2,
					"week": "2"
				},{
					"id": 3,
					"week": "3"
				},{
					"id": 4,
					"week": "4"
				},{
					"id": 5,
					"week": "5"
				},{
					"id": 6,
					"week": "6"
				},{
					"id": 7,
					"week": "7"
				},{
					"id": 8,
					"week": "8"
				},{
					"id": 9,
					"week": "9"
				},{
					"id": 10,
					"week": "10"
				},{
					"id": 11,
					"week": "11"
				},{
					"id": 12,
					"week": "12"
				},{
					"id": 13,
					"week": "13"
				},{
					"id": 14,
					"week": "14"
				},{
					"id": 15,
					"week": "15"
				},{
					"id": 16,
					"week": "16"
				},{
					"id": 17,
					"week": "17"
				},{
					"id": 18,
					"week": "18"
				},{
					"id": 19,
					"week": "19"
				},{
					"id": 20,
					"week": "20"
				}],
				section: [{
					"id": 1,
					"sec": "1-2"
				},{
					"id": 2,
					"sec": "3-4"
				},{
					"id": 3,
					"sec": "5-6"
				},{
					"id": 4,
					"sec": "7-8"
				},{
					"id": 5,
					"sec": "9-10"
				}],
				curriList: [],
				totle: 0,
				editCurriForm: {},
				addCurriForm: {
					kechengleibie: '选修',
				},
				editCurriDialogVisible: false,
				addCurriDialogVisible: false,
				editCurriFormRules: {
					kechengname: [
						{ required: true, message: '课程名称不能为空!',trigger: 'change' },
						{ min: 3 , max: 20 , message: '课程名称长度为 3 ~ 20个字!', trigger: 'change' }				
					],
					startweek: [
						{ required: true, message: '开始周次不能为空！',trigger: 'change' }	
					],
					endweek: [
						{ required: true ,validator: weekcheckend,trigger: 'change' }						
					]
				},
				addCurriFormRules: {				
					kechengname: [
						{ required: true, message: '课程名称不能为空!',trigger: 'change' },
						{ min: 3, max: 20 ,message: '课程名称长度为 3 ~ 20个字!',trigger: 'change' }
					],
					xuefen: [
						{ required: true, message: '课程学分不能为空!',trigger: 'change' }
					],
					classroom: [
						{ required: true, message: '上课教室不能为空!',trigger: 'change' }
					],
					startweek: [
						{ required: true, message: '开始周次不能为空！' ,trigger: 'change' }	
					],
					endweek: [
						{ required: true, validator: weekcheckAddend ,trigger: 'change' }						
					],
					classweek: [
						{ required: true, message: '上课周次不能为空!',trigger: 'change' }
					],
					classsection: [
						{ required: true, message: '上课节次不能为空!',trigger: 'change' }
					]
				},
				rooms: [],
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
				flag: '0',
				BanjiData: [],
				Optionalclass: []
			}
		},
		created() {
			this.saveNavState('/teacher/curriculum')
			this.getCurriList()
			this.getClassrooms()	
			// 判断当前的状态，是否需要打开编辑课程信息对话框，此处与“选课管理”页面关联
			if (window.sessionStorage.getItem('tag') === '1') {
				console.log(window.sessionStorage.getItem('tag'))
				this.flag = '1'
				this.showEditDialog(window.sessionStorage.getItem('classid')+'')				
			}else{
				if (window.sessionStorage.getItem('classid') != null) {
					console.log(window.sessionStorage.getItem('classid'))
					this.showEditDialog(window.sessionStorage.getItem('classid')+'')
					//window.sessionStorage.removeItem('classid')
				}
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
		methods: {
			// 获取当前账号开课信息
			getCurriList() {
				this.$axios.post('teacher/currilist',{
					pageNum: 		this.queryInfo.pagenum,
					pageSize: 		this.queryInfo.pagesize,
					kechengteacher: this.queryInfo.kechengteacher,
					kechengleibie: 	this.queryInfo.select_leibie,
					query: 			this.queryInfo.query,
				})
				.then(res => {

					if (res.status !== 200) {
						return this.$message.error("获取课程信息列表失败!")
					}
					this.curriList = res.data.records
					this.totle = res.data.total
					//console.log(res)
				})
				// 获取班级信息
				this.$axios.get('teacher/getbanjibyzh/' + window.sessionStorage.getItem("username"))
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取班级信息失败！")
					}
					this.BanjiData = res.data
				})
			},
			handleSizeChange (newSize) {
				this.queryInfo.pagesize = newSize
				this.getCurriList()
			},
			// 监听 页码值 改变事件
			handleCurrentChange (newSize) {
			  // console.log(newSize)
			  this.queryInfo.pagenum = newSize
			  this.getCurriList()
			},
			// 对课程名称进行模糊查询
			getCurriListLike() {
				this.getCurriList()
			},
			// 清除搜索按钮的清除事件
			clearValues() {
				this.queryInfo.query = '',
				this.queryInfo.select_leibie = ''
				this.getCurriList()
			},
			// 获取 教室数据
			getClassrooms() {
				// 发起请求获取教室数据
				this.$axios.get('teacher/getclassroomall')
				.then(res => {
					if(res.status !== 200){
						this.$message.error("获取上课教室数据失败！")
					}
					this.rooms = res.data
				})
			},		
			// 编辑课程信息
			showEditDialog(kechengid) {
				this.getClassrooms()
				
				this.$axios.get('teacher/getcurri/' + kechengid)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取课程信息失败！")
					}
					else {
						this.editCurriForm = res.data
						this.editCurriDialogVisible = true
						if (this.editCurriForm.optionalclass == null) {
							this.Optionalclass = ''
						}else {
							this.Optionalclass = this.editCurriForm.optionalclass.split(' ')
						}											
					}
				})
				window.sessionStorage.setItem('tag','1')
				if (this.flag === '1') {
					window.sessionStorage.removeItem('classid')
					window.sessionStorage.removeItem('tag')
				}						
				// 获取班级信息
				this.$axios.get('teacher/getbanjibyzh/' + window.sessionStorage.getItem("username"))
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取班级信息失败！")
					}
					this.BanjiData = res.data
				})
			},
			// 修改开设课程信息关闭事件
			quertexitCurri() {
				this.editCurriDialogVisible = false
				this.Optionalclass = ''
			},
			// 修改开设课程信息
			editCurri () {			
				var x = ''
				for(var i=0;i<this.Optionalclass.length;i++) {
					x = x + this.Optionalclass[i]
					if (i !== this.Optionalclass.length -1) {
						x = x + ' '
					}
				}
				this.editCurriForm.optionalclass = x			
				this.$refs.editCurriForm.validate(async valid => {
					if(valid) {
						this.$axios.put('teacher/updatecurri/' + this.editCurriForm.kechengid,this.editCurriForm)
						.then(res => {							
							if(res.status !== 200) {
								this.$message.error("更新开课信息失败！")
							}
							this.editCurriDialogVisible = false							
							this.$message.success("更新开课信息成功！")
							this.getCurriList()
						})
					}
				})
			},
			
		// 根据当前行课程ID删除课程信息
		async removeCurriByKechengid(kechengid) {
			const confirmResult = await this.$confirm('此操作将永久删除该课程信息，是否继续？','提示',{
				confirmButtonText: '确定',
				cancelButtonText:  '取消',
				type: 'warning'
			}).catch(err => err)
			if(confirmResult !== 'confirm') {
				return this.$message.info('已取消删除！')
			}
			this.$axios.delete('teacher/deletecurri/' + kechengid)
			.then(res => {
				if(res.status !== 200) {
					return this.$message.error('删除该课程信息失败！')
				}
				this.$message.success('删除该课程信息成功！')
				this.getCurriList()
			})
		},
		// 开设课程按钮事件
		newClassBtn() {
			this.getClassrooms()
			this.addCurriDialogVisible = true
			this.Optionalclass = ''
		},
		// 添加课程信息
		addCurri () {			
			var x = ''
			for(var i=0;i<this.Optionalclass.length;i++) {
				x = x + this.Optionalclass[i]
				if (i !== this.Optionalclass.length -1) {
					x = x + ' '
				}
			}
			this.addCurriForm.optionalclass = x
			this.addCurriForm.kechengteacher = this.queryInfo.kechengteacher
			this.$refs.addCurriForm.validate(async valid => {				
				if(valid) {
					this.$axios.post('admin/classroomcheck_curri',this.addCurriForm)
					.then(res => {
						if (res.status !== 200) {
							return this.$message.error("与服务器失去联系！请联系管理员！")
						}
						// console.log(this.addCurriForm)
						// console.log(res.data)
						if (res.data.length !== 0) {
							return this.$message.error("上课教室与其它课程冲突！请重新选择上课教室！")
						}
						this.$axios.post('teacher/addcurri',this.addCurriForm)
						.then(res => {
							if(res.status !== 200) {
								return this.$message.error('添加课程信息失败！')
							}
							if (res.status === 200) {
								this.addCurriDialogVisible = false
								this.$message.success("添加课程信息成功！")
								this.getCurriList()
							}
						})
					})
				}
			})
		},
		addCurriDialogClosed() {
			//取消表验证
			this.$refs['addCurriForm'].resetFields()
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
		
	}
</style>
