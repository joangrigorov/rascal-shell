/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Manifest;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.experiments.Compiler.Commands.Rascal;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalC;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalTests;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.compiled.CompiledREPLRunner;
import org.rascalmpl.uri.URIUtil;


public class RascalShell  {

    private static void printVersionNumber(){
        try {
            Enumeration<URL> resources = RascalShell.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                String bundleName = manifest.getMainAttributes().getValue("Bundle-Name");
                if (bundleName != null && bundleName.equals("rascal-shell")) {
                    String result = manifest.getMainAttributes().getValue("Bundle-Version");
                    if (result != null) {
                        System.out.println("Version: " + result);
                        return;
                    }
                }
            }
        } catch (IOException E) {
        }
        System.out.println("Version: unknown");
    }


    public static void main(String[] args) throws IOException {
        printVersionNumber();
        RascalManifest mf = new RascalManifest();

        try {
            ShellRunner runner; 
            if (mf.hasManifest(RascalShell.class) && mf.hasMainModule(RascalShell.class)) {
                runner = new ManifestRunner(mf, new PrintWriter(System.out), new PrintWriter(System.err, true));
            } 
            else if (args.length > 0) {            	
            	if (args[0].equals("--help")) {
                    System.err.println("Usage: java -jar rascal-version.jar [{--rascalc, --rascal, --rascalTests, --compiledREPL}] [Module]");
                    System.err.println("\ttry also the --help options of the respective commands.");
                    System.err.println("\tjava -jar rascal-version.jar [Module]: runs the main function of the module using the interpreter");
                    return;
                }
            	else if (args[0].equals("--rascalc")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) {
                            RascalC.main(Arrays.copyOfRange(args, 1, args.length));
                        }
                    };
                }
                else if (args[0].equals("--rascal")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) throws IOException {
                            Rascal.main(Arrays.copyOfRange(args, 1, args.length));
                        }
                    };
                }
                else if (args[0].equals("--rascalTests")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) throws IOException {
                            try {
                                RascalTests.main(Arrays.copyOfRange(args, 1, args.length));
                            } catch (NoSuchRascalFunction | URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                }
                else if (args[0].equals("--compiledREPL")) {
                    PathConfig pc = new PathConfig();
                    pc.addSourceLoc(URIUtil.correctLocation("file", null, Paths.get(".").toAbsolutePath().normalize().toString()));
                    runner = new CompiledREPLRunner(pc, System.in, System.out);
                    
                }
                else {
                    runner = new ModuleRunner(new PrintWriter(System.out), new PrintWriter(System.err, true));
                }
            } 
            else {
                runner = new REPLRunner(System.in, System.out);
            }
            runner.run(args);

            System.exit(0);
        }
        catch (Throwable e) {
            System.err.println("\n\nunexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
        finally {
            System.out.flush();
            System.err.flush();
        }
    }
}
