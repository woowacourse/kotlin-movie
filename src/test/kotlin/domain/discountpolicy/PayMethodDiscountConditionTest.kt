package domain.discountpolicy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PayMethodDiscountConditionTest {
    @Test
    fun `입력받은 결제 방식이 CARD라면 true를 반환한다`() {
        val payMethod = PayMethod.CARD

        val result = CardCondition().isSatisfiedBy(payMethod)

        assertThat(result).isTrue()
    }

    @Test
    fun `입력받은 결제 방식이 CASH라면 true를 반환한다`() {
        val payMethod = PayMethod.CASH

        val result = CashCondition().isSatisfiedBy(payMethod)

        assertThat(result).isTrue()
    }
}
