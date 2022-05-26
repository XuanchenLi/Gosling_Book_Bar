<template>
  <div class="search-employee">
    <HelloWorld></HelloWorld>
  </div>
</template>

<script>
import HelloWorld from '@/components/HelloWorld'

export default {
  name: 'SearchEmployeeView',
  components: {
    HelloWorld
  }
}
</script>

<style scoped>
.search-employee {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: 600px;
  float: right;
  margin-top: 40px;
  margin-right: 0;
  padding-right: 0;
}
</style>
