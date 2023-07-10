package nextstep.subway.unit;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LineTest {

    // given - line : 2호선, 상행역 : xx, 하행역 : xx, 거리 : xm 가 주어졌을 때
    // when - 노선에 구간을 추가하면
    // then - 구간 조회 시 상행역, 하행역을 찾을 수 있다

    /**
     * Section 추가 테스트 코드인데 Line추가 테스트 코드 같은 느낌이 든다, Line이 있다고 가정하고 Section을 추가해야 한다.
     * 그렇다면 방법은?
     * 인수테스트(사용자 요구사항 입장)이기 때문에 요청은 API로 호출하는 방식으로 하는게 맞아보인다.
     * 구간 조회 했는데 거리도 같이 알 수 있어야 올바른 테스트가 아닐까?
     *
     * 값이 셋팅되지 않은 상황에서 값으로 테스트를 하는 것은 의미가 없지 않을까? -> line이라는 없는 값으로 셋팅을 해야 될 때 어떻게 해?
     * 아니면 값을 직접 셋팅해주고 그 값에 맞게 테스트를 해야하는 걸까? stub을 사용하면 약속된 테스트를 할 수 있다.
     */
    @Test
    void addSection() {
        // given
        long lineId = 1L;
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("upStationId", 1L);
        parameter.put("downStationId", 2L);
        parameter.put("distance", 5);

        // when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(parameter)
                .post("/lines/" + lineId + "/sections")
                .then().log().all().extract();
        Assertions.assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> stations = RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all().extract();

        // then
        List<Long> names = stations.jsonPath().getList("id");
        Assertions.assertThat(names).containsAnyOf(1L);
        Assertions.assertThat(names).containsAnyOf(2L);
    }

    @Test
    void getStations() {
    }

    @Test
    void removeSection() {
    }
}
