<template>
  <div class="salary-view">
    <el-container class="salary-box" >
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>部门薪资统计</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="main" v-loading="mainLoad">
        <el-menu
          :default-active="'0'"
          class="menu"
          mode="horizontal"
          :ellipsis="false"
          @select="handleSelect"
        >
          <el-menu-item index="0">部门分布</el-menu-item>
          <el-menu-item index="1">薪资分布</el-menu-item>
        </el-menu>
        <el-card class="box-card" v-show="view[0]">
          <template #header>
            <div class="card-header">
              <span>人数分布</span>
            </div>
          </template>
          <div id="department" style="width: 800px;height:400px;"></div>
        </el-card>
        <el-card class="box-card"  v-show="view[1]">
          <template #header>
            <div class="card-header">
              <span>薪资分布</span>
            </div>
          </template>
          <div id="salary" style="width: 800px;height:400px;"></div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDepartmentStatsAPI } from '@/api/department'

export default {
  name: 'SalaryView',
  data() {
    return {
      departmentChart: null,
      salaryCharts: null,
      departmentStats: null,
      depOption: {
        tooltip : {
          show: true,
          trigger: 'item',
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: (params) => {
            let str='';
            for (let i in this.departmentStats) {
              if (this.departmentStats[i]['departmentName'] === params.name) {
                str += this.departmentStats[i]['positionName'] + ': '
                  + this.departmentStats[i]['number'] + '<br/>'
              }
            }
            return str
          }
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [
          {
            type: 'bar',
            data: []
          }
        ]
      },
      salaryOption: {

        tooltip : {
          show: true,
          trigger: 'item',
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: (params) => {
            let str1 = params.name + '<br/>' + '总额：'
            let str2 = ''
            let total = 0
            let people = 0
            for (let i in this.departmentStats) {
              if (this.departmentStats[i]['departmentName'] === params.name) {
                people += this.departmentStats[i]['number']
                total += this.departmentStats[i]['totalSalary']
                str2 += this.departmentStats[i]['positionName'] + ' 总额: '
                  + this.departmentStats[i]['totalSalary'] + '<br/>'
              }
            }
            str1 += total + '<br/>' + '人均：' + (people === 0 ? 0 : total / people)
            return str1 + '<br/>' + str2
          }
        },
        legend: {
          orient: 'vertical',
          x: 'right',
          data: [],
        },
        series: [

          {
            type: 'pie',
            data: [],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: true,
                //position: "inner",
                textStyle: {
                  fontWeight: 200,
                  fontSize: 18, //文字的字体大小
                },
                formatter: function (params) {
                  //console.log(params)
                  if (params.data.value === 0) {
                    //console.log(params.data)
                    params.data.labelLine.show = false
                    return ""
                  } else {
                    return params.name + '-' + params.percent + '%'
                  }
                }
              },
            }
          }
        ]
      },
      lastView: 0,
      view: [true, false],
      mainLoad: false,
    }
  },
  methods: {
    handleSelect(index) {
      this.view[this.lastView] = false
      this.view[index] = true
      this.lastView = index

    },
  },
  mounted () {
    this.lastView = 0;
    this.mainLoad = true;
    getDepartmentStatsAPI().then(
      (res) => {
        if (res.data.success) {
          let data = res.data.data
          this.departmentStats = data
          //console.log(data)
          for (let i = 0; i < data.length; ++i) {
            if (this.depOption.xAxis.data.indexOf(data[i]['departmentName']) === -1) {
              //console.log(data[i]['departmentName'])
              this.salaryOption.legend.data.push(data[i]['departmentName'])
              this.salaryOption.series[0]['data'].push(
                {
                  value: 0,
                  name: data[i]['departmentName'],
                  labelLine: {
                    show: true
                  }
                }
              )
              this.depOption.xAxis.data.push(data[i]['departmentName'])
              this.depOption.series[0]['data'].push(0)
              //console.log(this.depOption)
            }
            this.salaryOption.series[0]['data'][
              this.depOption.xAxis.data.indexOf(data[i]['departmentName'])
              ].value += data[i]['totalSalary']
            this.depOption.series[0]['data'][
              this.depOption.xAxis.data.indexOf(data[i]['departmentName'])
              ] += data[i]['number']
            this.departmentChart = echarts.init(document.getElementById('department'))
            this.departmentChart.setOption(this.depOption);
            this.salaryChart = echarts.init(document.getElementById('salary'))
            this.salaryChart.setOption(this.salaryOption);
          }

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
.salary-view {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: auto;
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

.menu .el-menu-item {
  background-color: #ffffff;

}
.box-card {
  width: 80%;
  margin-left: 10%;
  margin-top: 20px;
}
</style>
