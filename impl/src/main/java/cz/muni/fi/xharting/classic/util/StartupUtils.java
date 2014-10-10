/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.muni.fi.xharting.classic.util;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.xharting.classic.metadata.BeanDescriptor;
import cz.muni.fi.xharting.classic.metadata.MetadataRegistry;
import cz.muni.fi.xharting.classic.metadata.RoleDescriptor;

/**
 * Provides utilities for eager inititialization of managed beans.
 *
 * @author pan
 *
 */
public abstract class StartupUtils {

    private static final Logger log = LoggerFactory.getLogger(StartupUtils.class);

    /**
     * Initializes all startup beans (as returned by {@link MetadataRegistry}) in the specified scope.
     *
     * @param registry
     * @param scope
     * @param manager
     */
    public static void startup(MetadataRegistry registry, Class<? extends Annotation> scope, BeanManager manager) {
        if (registry == null) {
            throw new IllegalArgumentException("MetadataRegistry must be specified");
        }
        if (scope == null) {
            throw new IllegalArgumentException("Scope must be specified");
        }
        Collection<RoleDescriptor> startupBeans = registry.getStartupBeans(scope);
        if (startupBeans == null || startupBeans.isEmpty()) {
            return;
        }
        if (manager == null) {
            throw new IllegalArgumentException("BeanManager must be specified");
        }
        log.debug("Initializing {} {} startup beans...", startupBeans.size(), scope.getSimpleName());
        Set<String> started = new LinkedHashSet<String>(); // so that we do not start the component multiple times
        for (RoleDescriptor role : startupBeans) {
            startup(registry, role.getName(), started, manager);
        }
    }

    private static void startup(MetadataRegistry registry, String name, Set<String> started, BeanManager manager) {
        if (started.contains(name)) {
            return; // started already
        }
        BeanDescriptor descriptor = registry.getManagedBeanDescriptorByName(name);
        if (descriptor != null) {
            for (String dependency : descriptor.getStartupDependencies()) {
                startup(registry, dependency, started, manager);
            }
            if (descriptor.isStartup()) {
                Object reference = CdiUtils.lookupBeanByInternalName(name, Object.class, manager).getInstance();
                reference.toString(); // toString() hack to actually instantiate the instance behind proxy
                started.add(name);
            }
        }
    }
}
