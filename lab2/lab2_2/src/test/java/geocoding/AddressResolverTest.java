package geocoding;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddressResolverTest {

    AddressResolverService resolver;

    @Test
    @Disabled
    void whenResolveDetiGps_returnJacintoMagalhaeAddress() throws ParseException, IOException, URISyntaxException {

        //todo: implement test; remove Disabled annotation

        // will crash for now...need to set the resolver before using it
        Optional<Address> result = resolver.findAddressForLocation(40.63436, -8.65616);

        //return
        Address expected = new Address("Avenida da Universidade", "Aveiro", "3810-489", "");

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());

    }

    @Test
    @Disabled
    public void whenBadCoordidates_thenReturnNoValidAddress() throws IOException, URISyntaxException, ParseException {

        ///todo: implement test


        Optional<Address> result = resolver.findAddressForLocation(-361, -361);
        // verify no valid result
        assertFalse(result.isPresent());

    }
}