import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Events from './events';
import Tasks from './tasks';
import Gantry from './gantry';
import Vehicles from './vehicles';
import Images from './images';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="events/*" element={<Events />} />
        <Route path="tasks/*" element={<Tasks />} />
        <Route path="gantry/*" element={<Gantry />} />
        <Route path="vehicles/*" element={<Vehicles />} />
        <Route path="images/*" element={<Images />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
