import Vue from 'vue'
import VueRouter from 'vue-router'
// 登陆页面
import Login from '../components/Login.vue'
// 忘记密码页面
import Password from '../components/password.vue'
// （忘记密码）重置密码页面
import ResetPassword from '../components/ResetPassword.vue'


// 管理员	模板页
import AdminHome from '../components/sample/AdminHome.vue'
// 管理员	欢迎页面
import AdminWelcome from '../components/pages/admin/Welcome.vue'
// 管理员	教师管理页面
import Tea_Manage from '../components/pages/admin/Tea_manage.vue'
// 管理员 	学生管理页面
import Stu_Manage from '../components/pages/admin/Stu_manage.vue'
// 管理员	学院管理页面
import College_Manage from '../components/pages/admin/College_manage.vue'
// 管理员	专业管理页面
import Major_Manage from '../components/pages/admin/Major_manage.vue'
// 管理员	班级管理页面
import Banji_Manage from '../components/pages/admin/Banji_Manage.vue'
// 管理员   教室管理
import ClassRoom_Manage from '../components/pages/admin/Classroom_manage.vue'
// 管理员	个人中心页面
import AdminSelf from '../components/pages/admin/Adminself.vue'


// 教师		模板页
import TeacherHome from '../components/sample/TeacherHome.vue'
// 教师		欢迎页面
import TeaWelcome from '../components/pages/teacher/Welcome.vue'
//教师	 课程信息管理页面
import Curriculum from '../components/pages/teacher/Curriculum.vue'
//教师	 学生信息管理页面
import StudentManage from '../components/pages/teacher/Student_manage.vue'
//教师	选课信息管理页面
import CourseSelection from '../components/pages/teacher/Course_selection.vue'
//教师	个人中心页面
import TeacherSelf from '../components/pages/teacher/Teacherself.vue'


// 学生		模板页
import StudentHome from '../components/sample/StudentHome.vue'
// 学生		欢迎页面
import StuWelcome from '../components/pages/student/Welcome.vue'
//学生	个人中心页面
import StudentSelf from '../components/pages/student/Studentself.vue'
//学生	选课中心页面
import StuSelectCurri from '../components/pages/student/Select_curri.vue'
//学生	已选课信息页面
import StudentSelectionSelf from '../components/pages/student/Select_curriSelf.vue'
// 学生 我的课表页面
import Myclass from '../components/pages/student/Myclass.vue'




Vue.use(VueRouter)
const routes = [
	{
		path: '/',      // 根目录
		redirect: '/login'
	},
    {		
      path: '/login',
      component: Login
    },
	{	
	  path: '/teacher',					// 教师账户所包含的页面
	  name: 'TeacherHome',
	  component: TeacherHome,
	  children: [{
		  path: '/teacher/welcome',
		  name: 'TeaWelcome',
		  component: TeaWelcome
	  },
	  {
		  path: '/teacher/curriculum',
		  name: 'Curriculum',
		  component: Curriculum
	  },
	  {
		  path: '/teacher/student',
		  name: 'StudentManage',
		  component: StudentManage
	  },
	  {
		  path: '/teacher/course',
		  name: 'CourseSelection',
		  component: CourseSelection
	  },
	  {
		  path: '/teacher/self',
		  name: 'TeacherSelf',
		  component: TeacherSelf
	  }
	  ]
	},
	{
		path:'/pwd',				// 忘记密码/找回密码页面
		component:Password
	},
	{
		path: '/reset',
		component: ResetPassword
	},
	{
		path: '/student',
		name: 'StudentHome',
		component: StudentHome,
		children: [{
			path: '/student/welcome',
			name: 'StuWelcome',
			component: StuWelcome
		},{
			path: '/student/self',
			name: 'StudentSelf',
			component: StudentSelf
		},{
			path: '/student/course',
			name: 'StuSelectCurri',
			component: StuSelectCurri
		},{
			path: '/student/myclass',
			name: 'Myclass',
			component: Myclass
		},{
			path: '/student/courseself',
			name: 'StuSelectCurriSelf',
			component: StudentSelectionSelf
		}]
	},
	{
		path: '/admin',
		name: 'AdminHome',
		component: AdminHome,
		children: [{
			path: '/admin/welcome',
			name: 'AdminWelcome',
			component: AdminWelcome
		},{
			path: '/admin/teamanage',
			name: 'Tea_Manage',
			component: Tea_Manage
		},{
			path: '/admin/stumanage',
			name: 'Stu_Manage',
			component: Stu_Manage
		},
		{
			path: '/admin/collegemanage',
			name: 'College_Manage',
			component: College_Manage
		},
		{
			path: '/admin/majormanage',
			name: 'Major_Manage',
			component: Major_Manage
		},{
			path: '/admin/crmanage',
			name: 'ClassRoom_Manage',
			component: ClassRoom_Manage
		},
		{
			path: '/admin/banjimanage',
			name: 'Banji_Manage',
			component: Banji_Manage
		},{
			path: '/admin/self',
			name: 'AdminSelf',
			component: AdminSelf
		}]
	}
  ]

const router = new VueRouter({
  routes: routes,
  mode:"history"
})

// 挂载路由导航守卫,to表示将要访问的路径，from表示从哪里来，next是下一个要做的操作 next('/login')强制跳转login
router.beforeEach((to, from, next) => {
  // 访问登录页，放行
  if (to.path === '/login' || to.path === '/pwd' || to.path === '/reset') {
		return next()
  } 
  
  // // 获取token
  const tokenStr = window.sessionStorage.getItem('token')
  const typevalue = window.sessionStorage.getItem("typevalue")
  
  // 没有保存token即未登录
  if (!tokenStr) {
	  return next({path: '/login?x=1'})
  }
  
  
	   // 当前登录账户为 管理员 'admin'类型账户
  if(typevalue === 'admin' && (to.path === '/admin/welcome' || to.path === '/admin/teamanage' || to.path === '/admin/stumanage') || to.path === '/admin/collegemanage' || to.path === '/admin/majormanage' || to.path === '/admin/banjimanage' || to.path === '/admin/crmanage' || to.path === '/admin/self')  {	  
  	  next()
	  // 当前登录账户为 教师 'teacher'类型账户
  }else  if(typevalue === 'teacher' && (to.path === '/teacher/welcome' || to.path === '/teacher/student' || to.path === '/teacher/curriculum' || to.path === '/teacher/course' || to.path === '/teacher/self')) {
  	  next()
	  // 当前登录账户为 学生 'student'类型账户
  }else  if(typevalue === 'student' && (to.path === '/student/welcome' || to.path === '/student/course' || to.path === '/student/courseself' || to.path === '/student/myclass' || to.path === '/student/self')) {
  	  next()
  }else {	    	  
	  return next({path: from.fullPath})
  }
  
})


export default router
/*export default new VueRouter({
  routes: router,
  mode:"history"

})*/
