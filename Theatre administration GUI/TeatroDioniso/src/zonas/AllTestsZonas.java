/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ ButacaTest.class, DeshabilitacionButacaTest.class, PrecioZonaTest.class, ZSNoNumeradaTest.class,
                ZSNumeradaTest.class, ZonaCompuestaTest.class })

public class AllTestsZonas {

}
