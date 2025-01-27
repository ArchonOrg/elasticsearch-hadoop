/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.hadoop.util;

import java.io.Serializable;

import org.elasticsearch.hadoop.EsHadoopIllegalArgumentException;

/**
 * Elasticsearch major version information, useful to check client's query compatibility with the Rest API.
 */
public class EsMajorVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final EsMajorVersion V_0_X = new EsMajorVersion((byte) 0, "0.x");
    public static final EsMajorVersion V_1_X = new EsMajorVersion((byte) 1, "1.x");
    public static final EsMajorVersion V_2_X = new EsMajorVersion((byte) 2, "2.x");
    public static final EsMajorVersion V_5_X = new EsMajorVersion((byte) 5, "5.x");
    public static final EsMajorVersion V_6_X = new EsMajorVersion((byte) 6, "6.x");
    public static final EsMajorVersion V_7_X = new EsMajorVersion((byte) 7, "7.x");
    public static final EsMajorVersion LATEST = V_7_X;

    public final byte major;
    private final String version;

    private EsMajorVersion(byte major, String version) {
        this.major = major;
        this.version = version;
    }

    public boolean after(EsMajorVersion version) {
        return version.major < major;
    }

    public boolean on(EsMajorVersion version) {
        return version.major == major;
    }

    public boolean notOn(EsMajorVersion version) {
        return !on(version);
    }

    public boolean onOrAfter(EsMajorVersion version) {
        return version.major <= major;
    }

    public boolean before(EsMajorVersion version) {
        return version.major > major;
    }

    public boolean onOrBefore(EsMajorVersion version) {
        return version.major >= major;
    }

    public static EsMajorVersion parse(String version) {
        if (version.startsWith("0.")) {
            return new EsMajorVersion((byte) 0, version);
        }
        if (version.startsWith("1.")) {
            return new EsMajorVersion((byte) 1, version);
        }
        if (version.startsWith("2.")) {
            return new EsMajorVersion((byte) 2, version);
        }
        if (version.startsWith("5.")) {
            return new EsMajorVersion((byte) 5, version);
        }
        if (version.startsWith("6.")) {
            return new EsMajorVersion((byte) 6, version);
        }
        if (version.startsWith("7.")) {
            return new EsMajorVersion((byte) 7, version);
        }
        throw new EsHadoopIllegalArgumentException("Unsupported/Unknown Elasticsearch version [" + version + "]." +
                "Highest supported version is [" + LATEST.version + "]. You may need to upgrade ES-Hadoop.");
    }

    public int parseMinorVersion(String versionString) {
        String majorPrefix = "" + major + ".";
        if (versionString.startsWith(majorPrefix) == false) {
            throw new EsHadoopIllegalArgumentException("Invalid version string for major version; " +
                    "Received [" + versionString + "] for major version [" + version + "]");
        }
        String minorRemainder = versionString.substring(majorPrefix.length());
        int dot = minorRemainder.indexOf('.');
        if (dot < 1) {
            throw new EsHadoopIllegalArgumentException("Could not parse Elasticsearch minor version [" +
                    versionString + "]. Invalid version format.");
        }
        String rawMinorVersion = minorRemainder.substring(0, dot);
        try {
            return Integer.parseInt(rawMinorVersion);
        } catch (NumberFormatException e) {
            throw new EsHadoopIllegalArgumentException("Could not parse Elasticsearch minor version [" +
                    versionString + "]. Non-numeric minor version [" + rawMinorVersion + "].", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EsMajorVersion version = (EsMajorVersion) o;

        return major == version.major &&
                this.version.equals(version.version);
    }

    @Override
    public int hashCode() {
        return major;
    }

    @Override
    public String toString() {
        return version;
    }
}
