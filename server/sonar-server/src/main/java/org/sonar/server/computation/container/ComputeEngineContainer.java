/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.computation.container;

import org.sonar.core.platform.ComponentContainer;
import org.sonar.core.platform.ContainerPopulator;
import org.sonar.server.computation.queue.CeTask;

/**
 * The Compute Engine container. Created for a specific parent {@link ComponentContainer} and a specific {@link CeTask}.
 */
public interface ComputeEngineContainer extends ContainerPopulator.Container {

  ComponentContainer getParent();

  /**
   * Cleans up resources after process has been called and has returned.
   */
  void cleanup();

}
