<template>
  <div class="personal-salary">
    <el-container class="salary-box" >
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>薪资查询</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="main" v-loading="mainLoad">
        <el-row>
          <el-descriptions
            style="width: 100%;"
            title="当前薪资"
            :column="8"
            border
            direction="vertical"
            ref="currentSalary"
            id="currentSalary"
          >
            <template #extra>
              <el-button v-if="notPrint" type="primary" @click="this.savePdf('currentSalary', $event)">打印</el-button>
            </template>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  编号
                </div>
              </template>
                  {{this.employee.id}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  姓名
                </div>
              </template>
              {{this.employee.username}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  基础工资
                </div>
              </template>
              {{this.salary.basePay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  绩效奖金
                </div>
              </template>
              {{this.salary.meritPay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  补贴
                </div>
              </template>
              {{this.salary.subsidyPay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  加班费
                </div>
              </template>
              {{this.salary.overtimePay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  保险
                </div>
              </template>
              {{this.salary.insurancePay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  实发工资
                </div>
              </template>
              {{this.salary.basePay + this.salary.insurancePay + this.salary.meritPay
                + this.salary.overtimePay + this.salary.subsidyPay }}
            </el-descriptions-item>
          </el-descriptions>

        </el-row>
        <el-row>
          <el-col>
            <h3 style="float: left;">薪资发放历史查询</h3>
          </el-col>
        </el-row>
        <el-row class="time-block row-bg" >
          <el-col :span="5">
            <span style="line-height: 40px; float: right; ">发放日期:</span>
          </el-col>
          <el-col :span="6" style="margin-left: 2%;">
            <div class="start-block">
              <el-date-picker
                v-model="startDate"
                type="date"
                placeholder="Pick a day"
              />
            </div>
          </el-col>
          <el-col :span="1">
            <span style="line-height: 40px;">——</span>
          </el-col>
          <el-col :span="6">
            <div class="end-block">
              <el-date-picker
                v-model="endDate"
                type="date"
                placeholder="Pick a day"
              />
            </div>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" @click="clearDate">
              清空
            </el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="savePdf('history', $event)">
              打印
            </el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-table
            class="main-table"
            :data="salaryTable"
            style="margin-bottom: 20px; margin-top: 2%"
            row-key="id"
            stripe
            border
            default-expand-all
            max-height="500px"
            ref="history"
            id="history"
          >
            <el-table-column prop="id" align="center" label="编号"  />
            <el-table-column prop="basePay" align="center" label="基础工资"  />
            <el-table-column prop="meritPay" align="center" label="绩效奖金"  />
            <el-table-column prop="subsidyPay" align="center" label="补贴"  />
            <el-table-column prop="overtimePay" align="center" label="加班费"  />
            <el-table-column prop="insurancePay" align="center" label="保险"  />
            <el-table-column prop="sumPay" align="center" label="实发工资"  />
            <el-table-column prop="payTime" align="center" label="发放时间" width="100px" />
          </el-table>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { getPersonalSalaryAPI, getSalaryRecord } from '@/api/salary'
import { getEmployeeInfoAPI } from '@/api/employee'
import htmlToPDF from '@/utils/htmlToPDF'

export default {
  name: 'PersonalSalaryView',
  components: {

  },
  data () {
    return {
      notPrint: true,
      mainLoad: false,
      startDate: null,
      endDate: null,
      salary: {
        id: 0,
        employeeId: 0,
        basePay: 0,
        meritPay: 0,
        insurancePay: 0,
        subsidyPay: 0,
        overtimePay: 0,
      },
      employee: {
        id: 0,
        name: "NULL",
      },
      salaryTable: [],
    }
  },
  methods: {
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },
    clearDate() {
      this.startDate = null
      this.endDate = null
    },
    savePdf(refName, event){
      this.myBlur(event)
      if (refName === "currentSalary")
        this.notPrint = false
      //var self = this
      var t
      clearTimeout(t)
      t = setTimeout(function() {
        htmlToPDF.downloadPDF(document.querySelector('#' + refName), 'yourSalary')
        // window.eventHub.$emit('refreshList', '')// 更新右侧列表状态
        if (refName === "currentSalary")
          this.notPrint = true
      }, 500)
    },
  },
  mounted () {
    this.mainLoad = true
    getPersonalSalaryAPI().then(
      (res) => {
        if (res.data.success) {
          this.salary = res.data.data
          //console.log(this.salary)
          if (this.salary === null) return
          getEmployeeInfoAPI(this.salary.employeeId).then(
            (res) => {
              if (res.data.success) {
                this.employee = res.data.data
                //console.log(this.employee.id)
              }else {
                this.$message.error(res.data.message)
              }
            }
          )
        }else {
          this.$message.error(res.data.message)
        }
      }
    ).catch(
      (err) => {
        this.$message.error(err.message)
      }
    )
    const pack = {
      startDate: null,
      endDare: null
    }
    getSalaryRecord(pack).then(
      (res) => {
        if(res.data.success) {
          this.salaryTable = res.data.data
          this.mainLoad = false
        }else {
          this.$message.error(res.data.message)
        }
      }
    ).catch(
      (err) => {
        this.$message.error(err.message)
      }
    )
  }
}
</script>

<style scoped>
.personal-salary {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: 600px;
  float: right;
  margin-top: 40px;
  margin-right: 0;
  padding-right: 0;
}

.sub-header {
  background-color: white;
  height: 40px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.salary-box .main{
  width: 90%;
  margin-left: 5%;
  margin-top: 1%;
  overflow-y: hidden;
}
</style>
