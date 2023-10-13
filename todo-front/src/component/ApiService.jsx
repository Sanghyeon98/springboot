// API_BASE_URL 모듈에서 API의 기본 URL을 가져옵니다.
import { API_BASE_URL } from "./api-config";

// API 호출을 수행하는 함수를 정의합니다.
export function call(api, method, request) {
  // API 호출에 사용할 옵션 객체를 설정합니다.
  let options = {
    headers: new Headers({
      "Content-Type": "application/json", // 요청의 컨텐츠 타입을 JSON으로 설정합니다.
    }),
    url: API_BASE_URL + api, // API 엔드포인트 URL을 설정합니다.
    method: method, // HTTP 요청 메서드를 설정합니다 (GET, POST, PUT, DELETE 등).
  };

  // 요청 데이터가 존재하는 경우, 요청 데이터를 JSON 문자열로 변환하여 옵션에 추가합니다.
  if (request) {
    options.body = JSON.stringify(request);
  }

  // fetch 함수를 사용하여 API에 요청을 보내고, 응답을 처리합니다.
  return fetch(options.url, options)
    .then((response) => {
      return response.json(); // 응답 데이터를 JSON 형식으로 파싱하여 반환합니다.
    })
    .catch((error) => {
      console.log(error); // 오류가 발생한 경우 콘솔에 오류 메시지를 출력합니다.
    });
}
