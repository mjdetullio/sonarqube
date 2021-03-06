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
import React from 'react';

import { BarChart } from '../../../components/charts/bar-chart';
import { formatMeasure } from '../../../helpers/measures';
import { translateWithParameters } from '../../../helpers/l10n';


const HEIGHT = 80;


export const ComplexityDistribution = React.createClass({
  propTypes: {
    distribution: React.PropTypes.string.isRequired,
    of: React.PropTypes.string.isRequired
  },

  renderBarChart () {
    let data = this.props.distribution.split(';').map((point, index) => {
      let tokens = point.split('=');
      let y = parseInt(tokens[1], 10);
      let value = parseInt(tokens[0], 10);
      return {
        x: index,
        y: y,
        value: value,
        tooltip: translateWithParameters(`overview.complexity_tooltip.${this.props.of}`, y, value)
      };
    });

    let xTicks = data.map(point => point.value);

    let xValues = data.map(point => formatMeasure(point.y, 'INT'));

    return <BarChart data={data}
                     xTicks={xTicks}
                     xValues={xValues}
                     height={HEIGHT}
                     barsWidth={10}
                     padding={[25, 0, 25, 0]}/>;
  },

  render () {
    return <div className="overview-bar-chart">
      {this.renderBarChart()}
    </div>;
  }
});
