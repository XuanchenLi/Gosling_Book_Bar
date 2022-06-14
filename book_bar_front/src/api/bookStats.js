import http from '@/utils/http'
let request = "/testIp/"

export function getBookStatsSuggestAPI(params) {
  return http.get(`${request}/book/stats/suggest/${params}`)
}

export function getBookStatsInventoryAPI(params) {
  return http.get(`${request}/book/stats/${params}/inventory`)
}

export function getBookStatsSaleAPI(params) {
  return http.get(`${request}/book/stats/${params}/sale`)
}

export function getBookAllSaleAPI() {
  return http.get(`${request}/book/stats/all_sale`)
}

export function getBookTypeSaleAPI() {
  return http.get(`${request}/book/stats/type_sale`)
}
