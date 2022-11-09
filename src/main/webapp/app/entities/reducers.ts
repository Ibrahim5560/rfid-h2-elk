import events from 'app/entities/events/events.reducer';
import tasks from 'app/entities/tasks/tasks.reducer';
import gantry from 'app/entities/gantry/gantry.reducer';
import vehicles from 'app/entities/vehicles/vehicles.reducer';
import images from 'app/entities/images/images.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  events,
  tasks,
  gantry,
  vehicles,
  images,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
