package org.jboss.seam.classic.test.init.install;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;

@Name("charlie")
@Install(dependencies = "bravo")
class Bean5 {

}