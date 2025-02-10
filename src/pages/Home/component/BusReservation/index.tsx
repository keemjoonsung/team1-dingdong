
// 스타일
import {
    Detail,
    ReserveButton,
    ReserveButtonText,
    TextBox,
    Wrapper,
    Title
} from "@/pages/Home/component/BusReservation/styles.ts";



export default function BusReservation() {
    return (
        <Wrapper>
            <TextBox>
                <Title>
                    등하교 버스를 예매해볼까요?
                </Title>
                <Detail>
                    아직 예매 내역이 없어요!
                </Detail>
            </TextBox>
            <ReserveButton>
                <ReserveButtonText>
                    예매하러 가기
                </ReserveButtonText>
            </ReserveButton>
        </Wrapper>
    )
}