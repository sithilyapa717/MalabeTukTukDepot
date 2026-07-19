import org.junit.jupiter.api.Test;

import main.java.Dealer;

import static org.junit.jupiter.api.Assertions.*;

public class DealerTest {
    @Test
    void missingPhoneD(){
        Dealer d=new Dealer("D01","Malabe Aouto Spares",null,"Malabe");
        assetEquals("N/A",d.getPhone());
    }


    @Test
    void emptyLocation() {
        assertThrows(IllegalArgumentException.class, () ->
                new Dealer("D02","Some Traders","0771234567","  "));
    }

    @Test
    void idIsTrimmedAndUppercased() {
        Dealer d = new Dealer(" d03 ", "Kandy Motors", "0772223333", "Kandy");
        assertEquals("D03", d.getId());
    }
}
