import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './task.reducer';

export const TaskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const taskEntity = useAppSelector(state => state.task.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskDetailsHeading">
          <Translate contentKey="rfidh2ElkApp.task.detail.title">Task</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskEntity.id}</dd>
          <dt>
            <span id="guid">
              <Translate contentKey="rfidh2ElkApp.task.guid">Guid</Translate>
            </span>
          </dt>
          <dd>{taskEntity.guid}</dd>
          <dt>
            <span id="plate">
              <Translate contentKey="rfidh2ElkApp.task.plate">Plate</Translate>
            </span>
          </dt>
          <dd>{taskEntity.plate}</dd>
          <dt>
            <span id="imageLp">
              <Translate contentKey="rfidh2ElkApp.task.imageLp">Image Lp</Translate>
            </span>
          </dt>
          <dd>
            {taskEntity.imageLp ? (
              <div>
                {taskEntity.imageLpContentType ? (
                  <a onClick={openFile(taskEntity.imageLpContentType, taskEntity.imageLp)}>
                    <img src={`data:${taskEntity.imageLpContentType};base64,${taskEntity.imageLp}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {taskEntity.imageLpContentType}, {byteSize(taskEntity.imageLp)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="imageThumb">
              <Translate contentKey="rfidh2ElkApp.task.imageThumb">Image Thumb</Translate>
            </span>
          </dt>
          <dd>
            {taskEntity.imageThumb ? (
              <div>
                {taskEntity.imageThumbContentType ? (
                  <a onClick={openFile(taskEntity.imageThumbContentType, taskEntity.imageThumb)}>
                    <img src={`data:${taskEntity.imageThumbContentType};base64,${taskEntity.imageThumb}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {taskEntity.imageThumbContentType}, {byteSize(taskEntity.imageThumb)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="anpr">
              <Translate contentKey="rfidh2ElkApp.task.anpr">Anpr</Translate>
            </span>
          </dt>
          <dd>{taskEntity.anpr}</dd>
          <dt>
            <span id="rfid">
              <Translate contentKey="rfidh2ElkApp.task.rfid">Rfid</Translate>
            </span>
          </dt>
          <dd>{taskEntity.rfid}</dd>
          <dt>
            <span id="dataStatus">
              <Translate contentKey="rfidh2ElkApp.task.dataStatus">Data Status</Translate>
            </span>
          </dt>
          <dd>{taskEntity.dataStatus}</dd>
          <dt>
            <span id="gantry">
              <Translate contentKey="rfidh2ElkApp.task.gantry">Gantry</Translate>
            </span>
          </dt>
          <dd>{taskEntity.gantry}</dd>
          <dt>
            <span id="kph">
              <Translate contentKey="rfidh2ElkApp.task.kph">Kph</Translate>
            </span>
          </dt>
          <dd>{taskEntity.kph}</dd>
          <dt>
            <span id="ambush">
              <Translate contentKey="rfidh2ElkApp.task.ambush">Ambush</Translate>
            </span>
          </dt>
          <dd>{taskEntity.ambush}</dd>
          <dt>
            <span id="direction">
              <Translate contentKey="rfidh2ElkApp.task.direction">Direction</Translate>
            </span>
          </dt>
          <dd>{taskEntity.direction}</dd>
          <dt>
            <span id="vehicle">
              <Translate contentKey="rfidh2ElkApp.task.vehicle">Vehicle</Translate>
            </span>
          </dt>
          <dd>{taskEntity.vehicle}</dd>
          <dt>
            <span id="issue">
              <Translate contentKey="rfidh2ElkApp.task.issue">Issue</Translate>
            </span>
          </dt>
          <dd>{taskEntity.issue}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rfidh2ElkApp.task.status">Status</Translate>
            </span>
          </dt>
          <dd>{taskEntity.status}</dd>
          <dt>
            <span id="handledBy">
              <Translate contentKey="rfidh2ElkApp.task.handledBy">Handled By</Translate>
            </span>
          </dt>
          <dd>{taskEntity.handledBy}</dd>
          <dt>
            <span id="gantryProcessed">
              <Translate contentKey="rfidh2ElkApp.task.gantryProcessed">Gantry Processed</Translate>
            </span>
          </dt>
          <dd>{taskEntity.gantryProcessed}</dd>
          <dt>
            <span id="gantrySent">
              <Translate contentKey="rfidh2ElkApp.task.gantrySent">Gantry Sent</Translate>
            </span>
          </dt>
          <dd>{taskEntity.gantrySent}</dd>
          <dt>
            <span id="when">
              <Translate contentKey="rfidh2ElkApp.task.when">When</Translate>
            </span>
          </dt>
          <dd>{taskEntity.when}</dd>
          <dt>
            <span id="toll">
              <Translate contentKey="rfidh2ElkApp.task.toll">Toll</Translate>
            </span>
          </dt>
          <dd>{taskEntity.toll}</dd>
          <dt>
            <span id="ruleRcvd">
              <Translate contentKey="rfidh2ElkApp.task.ruleRcvd">Rule Rcvd</Translate>
            </span>
          </dt>
          <dd>{taskEntity.ruleRcvd}</dd>
          <dt>
            <span id="wantedFor">
              <Translate contentKey="rfidh2ElkApp.task.wantedFor">Wanted For</Translate>
            </span>
          </dt>
          <dd>{taskEntity.wantedFor}</dd>
          <dt>
            <span id="fine">
              <Translate contentKey="rfidh2ElkApp.task.fine">Fine</Translate>
            </span>
          </dt>
          <dd>{taskEntity.fine}</dd>
          <dt>
            <span id="licenseIssue">
              <Translate contentKey="rfidh2ElkApp.task.licenseIssue">License Issue</Translate>
            </span>
          </dt>
          <dd>{taskEntity.licenseIssue}</dd>
          <dt>
            <span id="wantedBy">
              <Translate contentKey="rfidh2ElkApp.task.wantedBy">Wanted By</Translate>
            </span>
          </dt>
          <dd>{taskEntity.wantedBy}</dd>
          <dt>
            <span id="ruleProcessed">
              <Translate contentKey="rfidh2ElkApp.task.ruleProcessed">Rule Processed</Translate>
            </span>
          </dt>
          <dd>{taskEntity.ruleProcessed}</dd>
          <dt>
            <span id="speedFine">
              <Translate contentKey="rfidh2ElkApp.task.speedFine">Speed Fine</Translate>
            </span>
          </dt>
          <dd>{taskEntity.speedFine}</dd>
          <dt>
            <span id="lane">
              <Translate contentKey="rfidh2ElkApp.task.lane">Lane</Translate>
            </span>
          </dt>
          <dd>{taskEntity.lane}</dd>
          <dt>
            <span id="tagIssue">
              <Translate contentKey="rfidh2ElkApp.task.tagIssue">Tag Issue</Translate>
            </span>
          </dt>
          <dd>{taskEntity.tagIssue}</dd>
          <dt>
            <span id="statusName">
              <Translate contentKey="rfidh2ElkApp.task.statusName">Status Name</Translate>
            </span>
          </dt>
          <dd>{taskEntity.statusName}</dd>
          <dt>
            <span id="licenseFine">
              <Translate contentKey="rfidh2ElkApp.task.licenseFine">License Fine</Translate>
            </span>
          </dt>
          <dd>{taskEntity.licenseFine}</dd>
          <dt>
            <span id="stolen">
              <Translate contentKey="rfidh2ElkApp.task.stolen">Stolen</Translate>
            </span>
          </dt>
          <dd>{taskEntity.stolen}</dd>
          <dt>
            <span id="wanted">
              <Translate contentKey="rfidh2ElkApp.task.wanted">Wanted</Translate>
            </span>
          </dt>
          <dd>{taskEntity.wanted ? 'true' : 'false'}</dd>
          <dt>
            <span id="ruleSent">
              <Translate contentKey="rfidh2ElkApp.task.ruleSent">Rule Sent</Translate>
            </span>
          </dt>
          <dd>{taskEntity.ruleSent}</dd>
          <dt>
            <span id="handled">
              <Translate contentKey="rfidh2ElkApp.task.handled">Handled</Translate>
            </span>
          </dt>
          <dd>{taskEntity.handled}</dd>
          <dt>
            <span id="ruleIssue">
              <Translate contentKey="rfidh2ElkApp.task.ruleIssue">Rule Issue</Translate>
            </span>
          </dt>
          <dd>{taskEntity.ruleIssue}</dd>
          <dt>
            <span id="processingTime">
              <Translate contentKey="rfidh2ElkApp.task.processingTime">Processing Time</Translate>
            </span>
          </dt>
          <dd>{taskEntity.processingTime}</dd>
          <dt>
            <span id="threadRandNo">
              <Translate contentKey="rfidh2ElkApp.task.threadRandNo">Thread Rand No</Translate>
            </span>
          </dt>
          <dd>{taskEntity.threadRandNo}</dd>
        </dl>
        <Button tag={Link} to="/task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task/${taskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaskDetail;
