/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.clustering;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * A member in a Carbon cluster
 */
public class ClusterMember implements Serializable {

    /**
     * The host name of this member. Can be the name or the IP address
     */
    private String hostName;

    /**
     * The TCP port used by this member for communicating clustering messages
     */
    private int port = -1;

    /**
     * Indicates whether this member is ACTIVE or PASSIVE
     */
    private boolean isActive = true;

    /**
     * The domain of this member
     */
    private String domain;

    /**
     * Other member specific properties
     */
    private Properties properties = new Properties();


    /**
     * Time at which this member was suspended
     */
    private long suspendedTime = -1;

    /**
     * Time in millis which this member should be suspended
     */
    private long suspensionDuration = -1;


    private String remoteHost;

    private String id;

    private InetSocketAddress inetSocketAddress;


    public ClusterMember(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.inetSocketAddress = new InetSocketAddress(hostName, port);
    }

    /**
     * Temporarily suspend this member
     *
     * @param suspensionDurationMillis The time duration in millis in which this
     *                                 member should be suspended
     */
    public void suspend(long suspensionDurationMillis) {
        this.suspendedTime = System.currentTimeMillis();
        this.suspensionDuration = suspensionDurationMillis;
    }

    /**
     * Check whether this member is suspended
     *
     * @return true if this member is still suspended, false oterwise
     */
    public boolean isSuspended() {
        if (suspendedTime == -1) {
            return false;
        }
        if (System.currentTimeMillis() - suspendedTime >= suspensionDuration) {
            this.suspendedTime = -1;
            this.suspensionDuration = -1;
            return false;
        }
        return true;
    }

    public String getHostName() {
        String remoteHost = properties.getProperty("remoteHost");
        if (remoteHost != null) {
            return remoteHost;
        }
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }


    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClusterMember member = (ClusterMember) o;

        boolean isIdenticalPorts = port == member.getPort();

        return (isIdenticalPorts &&
                (remoteHost != null ? remoteHost.equals(member.getRemoteHost()) :
                 member.getRemoteHost() == null) && (hostName != null ?
                                                     hostName.equals(member.getHostName()) :
                                                     member.getHostName() == null));
    }

    public int hashCode() {
        int result;
        result = (hostName != null ? hostName.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }

    public String toString() {
        return "Host:" + hostName + ", Port: " + port + ", Domain: " + domain +
               ", Sub-domain:" + properties.getProperty("sub-domain")+ ", Active:" + isActive;
    }
}
