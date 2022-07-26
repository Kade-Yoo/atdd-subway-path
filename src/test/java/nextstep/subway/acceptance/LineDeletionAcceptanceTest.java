package nextstep.subway.acceptance;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.fake.FakeLineFactory;
import nextstep.subway.fake.FakeStationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineDeletionAcceptanceTest extends AcceptanceTest{

    private Station 강남역;
    private Station 구의역;
    private Station 역삼역;
    private Station 신촌역;
    private Station 선릉역;
    private Long 신분당선_ID;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationSteps.지하철역_생성_요청(FakeStationFactory.강남역().getName()).as(Station.class);
        구의역 = StationSteps.지하철역_생성_요청(FakeStationFactory.구의역().getName()).as(Station.class);
        역삼역 = StationSteps.지하철역_생성_요청(FakeStationFactory.역삼역().getName()).as(Station.class);
        선릉역 = StationSteps.지하철역_생성_요청(FakeStationFactory.선릉역().getName()).as(Station.class);
        신촌역 = StationSteps.지하철역_생성_요청(FakeStationFactory.신촌역().getName()).as(Station.class);
        신분당선_ID = LineSteps.지하철_노선_생성_요청(
                                   FakeLineFactory.신분당선().getName(),
                                   FakeLineFactory.신분당선().getColor()).jsonPath().getLong("id");
    }

    /* given 강남_구의, 구의_역삼 구간을 등록한다.
     * when 구의역을 삭제한다.
     * then 구간을 조회하고 구의역이 정상적으로 삭제되었는지 확인한다.
     */
    @Test
    @DisplayName("지하철 구간 삭제 테스트 - 중간에 있는 구간을 삭제한다")
    void deleteMiddleStation() {
        //given
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선_ID,createAdditionalDto(강남역.getId(), 구의역.getId(), 10));
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선_ID, createAdditionalDto(구의역.getId(), 역삼역.getId(), 10));

        //when
        LineSteps.지하철_노선에_지하철_구간_제거_요청(신분당선_ID, 구의역.getId());

        //then
        List<String> names = LineSteps.지하철_노선_조회_요청(신분당선_ID)
                                      .jsonPath().getList("stations.name", String.class);
        assertThat(names).containsExactly("강남역", "역삼역");
    }


    /* given 강남_구의, 구의_역삼 구간을 등록한다.
     * when 역삼역을 삭제한다.
     * then 구간을 조회하고 구의역이 정상적으로 삭제되었는지 확인한다.
     */
    @Test
    @DisplayName("지하철 구간 삭제 테스트 - 하행 종점역을 삭제한다")
    void deleteDownTerminusStation() {
        //given
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선_ID,createAdditionalDto(강남역.getId(), 구의역.getId(), 10));
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선_ID, createAdditionalDto(구의역.getId(), 역삼역.getId(), 10));

        //when
        LineSteps.지하철_노선에_지하철_구간_제거_요청(신분당선_ID, 역삼역.getId());

        //then
        List<String> names = LineSteps.지하철_노선_조회_요청(신분당선_ID)
                                      .jsonPath().getList("stations.name", String.class);
        assertThat(names).containsExactly("강남역", "구의역");
    }

    /* given 강남_구의, 구의_역삼 구간을 등록한다.
     * when 강남역을 삭제한다.
     * then 구간을 조회하고 구의역이 정상적으로 삭제되었는지 확인한다.
     */
    @Test
    @DisplayName("지하철 구간 삭제 테스트 - 상행 종점역을 삭제한다")
    void deleteUpTerminusStation() {
        //given
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선_ID,createAdditionalDto(강남역.getId(), 구의역.getId(), 10));
        LineSteps.지하철_노선에_지하철_구간_생성_요청(신분당선_ID, createAdditionalDto(구의역.getId(), 역삼역.getId(), 10));

        //when
        LineSteps.지하철_노선에_지하철_구간_제거_요청(신분당선_ID, 강남역.getId());

        //then
        List<String> names = LineSteps.지하철_노선_조회_요청(신분당선_ID)
                                      .jsonPath().getList("stations.name", String.class);
        assertThat(names).containsExactly("구의역", "역삼역");
    }


    private SectionRequest createAdditionalDto(Long upStationId, Long downStationId, Integer distance) {
        return new SectionRequest(upStationId, downStationId, distance);
    }

}
