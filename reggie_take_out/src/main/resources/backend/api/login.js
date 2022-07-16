function loginApi(data) {
  return $axios({
    'url': '/employee/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}

function checkOut(nums){
  return $axios({
    url:'/employee/check',
    method:'get',
    data:nums
  })
}
