/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import connection.TqsBasicHttpClient;
import geocoding.Address;
import geocoding.AddressResolverService;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.Optional;


/**
 * demonstrate the geocoding service.
 * Note that the existence of proper tests makes this Demo classes unnecessary!
 * Tests already include demonstration scenarios.
 *
 * @author ico
 */
public class GeocodeDemoMain {

    static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * demo for address resolver
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AddressResolverService resolver = new AddressResolverService(new TqsBasicHttpClient());
            Optional<Address> result = resolver.findAddressForLocation(40.63436, -8.65616);

            log.info("Result: ".concat(result.get().toString()));

        } catch (URISyntaxException | IOException | ParseException | org.json.simple.parser.ParseException ex) {
            log.error(String.valueOf(ex));
        }
    }

}
