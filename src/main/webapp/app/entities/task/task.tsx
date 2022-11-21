import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { openFile, byteSize, Translate, translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITask } from 'app/shared/model/task.model';
import { searchEntities, getEntities } from './task.reducer';

export const Task = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const taskList = useAppSelector(state => state.task.entities);
  const loading = useAppSelector(state => state.task.loading);
  const totalItems = useAppSelector(state => state.task.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="task-heading" data-cy="TaskHeading">
        <Translate contentKey="rfidh2ElkApp.task.home.title">Tasks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rfidh2ElkApp.task.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/task/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rfidh2ElkApp.task.home.createLabel">Create new Task</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('rfidh2ElkApp.task.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {taskList && taskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rfidh2ElkApp.task.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('guid')}>
                  <Translate contentKey="rfidh2ElkApp.task.guid">Guid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('plate')}>
                  <Translate contentKey="rfidh2ElkApp.task.plate">Plate</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imageLp')}>
                  <Translate contentKey="rfidh2ElkApp.task.imageLp">Image Lp</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imageThumb')}>
                  <Translate contentKey="rfidh2ElkApp.task.imageThumb">Image Thumb</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('anpr')}>
                  <Translate contentKey="rfidh2ElkApp.task.anpr">Anpr</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rfid')}>
                  <Translate contentKey="rfidh2ElkApp.task.rfid">Rfid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dataStatus')}>
                  <Translate contentKey="rfidh2ElkApp.task.dataStatus">Data Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantry')}>
                  <Translate contentKey="rfidh2ElkApp.task.gantry">Gantry</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('kph')}>
                  <Translate contentKey="rfidh2ElkApp.task.kph">Kph</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ambush')}>
                  <Translate contentKey="rfidh2ElkApp.task.ambush">Ambush</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('direction')}>
                  <Translate contentKey="rfidh2ElkApp.task.direction">Direction</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('vehicle')}>
                  <Translate contentKey="rfidh2ElkApp.task.vehicle">Vehicle</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('issue')}>
                  <Translate contentKey="rfidh2ElkApp.task.issue">Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="rfidh2ElkApp.task.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('handledBy')}>
                  <Translate contentKey="rfidh2ElkApp.task.handledBy">Handled By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantryProcessed')}>
                  <Translate contentKey="rfidh2ElkApp.task.gantryProcessed">Gantry Processed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantrySent')}>
                  <Translate contentKey="rfidh2ElkApp.task.gantrySent">Gantry Sent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('when')}>
                  <Translate contentKey="rfidh2ElkApp.task.when">When</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('toll')}>
                  <Translate contentKey="rfidh2ElkApp.task.toll">Toll</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleRcvd')}>
                  <Translate contentKey="rfidh2ElkApp.task.ruleRcvd">Rule Rcvd</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wantedFor')}>
                  <Translate contentKey="rfidh2ElkApp.task.wantedFor">Wanted For</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fine')}>
                  <Translate contentKey="rfidh2ElkApp.task.fine">Fine</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('licenseIssue')}>
                  <Translate contentKey="rfidh2ElkApp.task.licenseIssue">License Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wantedBy')}>
                  <Translate contentKey="rfidh2ElkApp.task.wantedBy">Wanted By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleProcessed')}>
                  <Translate contentKey="rfidh2ElkApp.task.ruleProcessed">Rule Processed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('speedFine')}>
                  <Translate contentKey="rfidh2ElkApp.task.speedFine">Speed Fine</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lane')}>
                  <Translate contentKey="rfidh2ElkApp.task.lane">Lane</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tagIssue')}>
                  <Translate contentKey="rfidh2ElkApp.task.tagIssue">Tag Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('statusName')}>
                  <Translate contentKey="rfidh2ElkApp.task.statusName">Status Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('licenseFine')}>
                  <Translate contentKey="rfidh2ElkApp.task.licenseFine">License Fine</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stolen')}>
                  <Translate contentKey="rfidh2ElkApp.task.stolen">Stolen</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wanted')}>
                  <Translate contentKey="rfidh2ElkApp.task.wanted">Wanted</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleSent')}>
                  <Translate contentKey="rfidh2ElkApp.task.ruleSent">Rule Sent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('handled')}>
                  <Translate contentKey="rfidh2ElkApp.task.handled">Handled</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleIssue')}>
                  <Translate contentKey="rfidh2ElkApp.task.ruleIssue">Rule Issue</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('processingTime')}>
                  <Translate contentKey="rfidh2ElkApp.task.processingTime">Processing Time</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('threadRandNo')}>
                  <Translate contentKey="rfidh2ElkApp.task.threadRandNo">Thread Rand No</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taskList.map((task, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/task/${task.id}`} color="link" size="sm">
                      {task.id}
                    </Button>
                  </td>
                  <td>{task.guid}</td>
                  <td>{task.plate}</td>
                  <td>
                    {task.imageLp ? (
                      <div>
                        {task.imageLpContentType ? (
                          <a onClick={openFile(task.imageLpContentType, task.imageLp)}>
                            <img src={`data:${task.imageLpContentType};base64,${task.imageLp}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {task.imageLpContentType}, {byteSize(task.imageLp)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {task.imageThumb ? (
                      <div>
                        {task.imageThumbContentType ? (
                          <a onClick={openFile(task.imageThumbContentType, task.imageThumb)}>
                            <img src={`data:${task.imageThumbContentType};base64,${task.imageThumb}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {task.imageThumbContentType}, {byteSize(task.imageThumb)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{task.anpr}</td>
                  <td>{task.rfid}</td>
                  <td>{task.dataStatus}</td>
                  <td>{task.gantry}</td>
                  <td>{task.kph}</td>
                  <td>{task.ambush}</td>
                  <td>{task.direction}</td>
                  <td>{task.vehicle}</td>
                  <td>{task.issue}</td>
                  <td>{task.status}</td>
                  <td>{task.handledBy}</td>
                  <td>{task.gantryProcessed}</td>
                  <td>{task.gantrySent}</td>
                  <td>{task.when}</td>
                  <td>{task.toll}</td>
                  <td>{task.ruleRcvd}</td>
                  <td>{task.wantedFor}</td>
                  <td>{task.fine}</td>
                  <td>{task.licenseIssue}</td>
                  <td>{task.wantedBy}</td>
                  <td>{task.ruleProcessed}</td>
                  <td>{task.speedFine}</td>
                  <td>{task.lane}</td>
                  <td>{task.tagIssue}</td>
                  <td>{task.statusName}</td>
                  <td>{task.licenseFine}</td>
                  <td>{task.stolen}</td>
                  <td>{task.wanted ? 'true' : 'false'}</td>
                  <td>{task.ruleSent}</td>
                  <td>{task.handled}</td>
                  <td>{task.ruleIssue}</td>
                  <td>{task.processingTime}</td>
                  <td>{task.threadRandNo}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/task/${task.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/task/${task.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/task/${task.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="rfidh2ElkApp.task.home.notFound">No Tasks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={taskList && taskList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Task;
