/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.compiler.runtime.pipeline.impl;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.SAXParseException;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.ErrorReceiver;

/**
 *
 */
public class DroolsJaxbHelperXjcClasses {

    public static class JaxbErrorReceiver4Drools extends ErrorReceiver {
    
        public String stage = "processing";
    
        public void warning(SAXParseException e) {
            e.printStackTrace();
        }
    
        public void error(SAXParseException e) {
            e.printStackTrace();
        }
    
        public void fatalError(SAXParseException e) {
            e.printStackTrace();
        }
    
        public void info(SAXParseException e) {
            e.printStackTrace();
        }
    }

    public static class MapVfsCodeWriter extends CodeWriter {
    
        private final Map<String, byte[]> map;
    
        private ByteArrayOutputStream     currentBaos;
        private String                    currentPath;
    
        public MapVfsCodeWriter() {
            this.map = new LinkedHashMap<String, byte[]>();
        }
    
        public OutputStream openBinary(JPackage pkg,
                                       String fileName) throws IOException {
            String pkgName = pkg.name();
    
            if ( pkgName.length() != 0 ) {
                pkgName += '.';
            }
    
            if ( this.currentBaos != null ) {
                this.currentBaos.close();
                this.map.put( this.currentPath,
                              this.currentBaos.toByteArray() );
            }
    
            this.currentPath = pkgName + fileName;
            this.currentBaos = new ByteArrayOutputStream();
    
            return new FilterOutputStream( this.currentBaos ) {
                public void close() {
                    // don't let this stream close
                }
            };
        }
    
        public void close() throws IOException {
            if ( this.currentBaos != null ) {
                this.currentBaos.close();
                this.map.put( this.currentPath,
                              this.currentBaos.toByteArray() );
            }
        }
    
        public Map<String, byte[]> getMap() {
            return this.map;
        }
    
    }

}
