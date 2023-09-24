import es.uam.eps.padsof.telecard.*;

public class MainCardPayment {
	public static void main(String[] args) throws InvalidCardNumberException, FailedInternetConnectionException, OrderRejectedException {
		// card numbers of 16 digits are valid
		System.out.println(TeleChargeAndPaySystem.isValidCardNumber("1234567890123456"));
		System.out.println(TeleChargeAndPaySystem.isValidCardNumber("123456789012345"));
		TeleChargeAndPaySystem.charge("1234567890123456", "Annual pass", 350, true);
	}
}
