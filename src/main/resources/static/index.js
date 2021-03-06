const ResponseType = {
  STAGE_1: 'STAGE_1',
  STAGE_2: 'STAGE_2',
  STAGE_3: 'STAGE_3',
}


class Entity {
  id = null
  nameElement = null
  checkboxElement = null
  startButton = null
  statusLabel = null

  constructor(id, nameElement, checkboxElement, startButtonElement, statusLabelElement) {
    this.id = id
    this.nameElement = nameElement
    this.checkboxElement = checkboxElement
    this.startButton = startButtonElement
    this.statusLabel = statusLabelElement
  }

  updateLabel(value) {
    this.statusLabel.value = value
  }

  addStartButtonListener(event, callback) {
    this.startButton.addEventListener(event, callback)
  }

  get name() {
    return this.nameElement.innerHTML
  }

  get isSelected() {
    return this.checkboxElement.checked
  }

}

class ProcessEntityRequest {
  name = null
  topic = "/app/process/entity"

  constructor(name) {
    this.name = name
  }

  serialize() {
    return JSON.stringify({name: this.name})
  }

  get topic() {
    return this.topic
  }
}

class ProcessEntitiesRequest {
  _entities = []
  _topic = "/app/process/entities"

  addEntity(name) {
    this.entities.push(name)
  }

  serialize() {
    return JSON.stringify(this.entities.map(name => ({name})))
  }

  get entities() {
    return this._entities
  }

  get topic() {
    return this._topic
  }
}

class WebsocketManager {
  stompClient = null
  updateStageCallback = null

  connect() {
    let socket = new SockJS("/ws")
    this.stompClient = Stomp.over(socket)
    this.stompClient.connect({}, this.applySubscriptions.bind(this))
  }

  setUpdateStageCallback(callback) {
    this.updateStageCallback = callback
  }

  applySubscriptions() {
    //  * right now let's do hard coded subscriptions, but really we should add an `addSubscription`
    //  * method and then keep an array of subscriptions to apply

    this.stompClient.subscribe('/topic/messages', (response) => {
      const messageData = JSON.parse(response.body)
      this.handleServerMessage(messageData)
    })
  }

  sendMessage(message) {
    console.log("sending message")
    console.log(message.serialize())
    this.stompClient.send(message.topic, {}, message.serialize())
  }

  handleServerMessage(message) {
    console.log(`Message type: ${message.type} sent from user: ${message.from} for entity: ${message.entityName} `)
    switch (message.type) {
      case ResponseType.STAGE_1:
        console.log("stage 1")
        this.updateStageCallback(message.entityName, "purple")
        break
      case ResponseType.STAGE_2:
        console.log("stage 2")
        this.updateStageCallback(message.entityName, "orange")
        break
      case ResponseType.STAGE_3:
        console.log("stage 3")
        this.updateStageCallback(message.entityName, "green")
        break
    }
  }
}

class EntityProcessor {
  websocketManager = null

  constructor(websocketManager) {
    this.websocketManager = websocketManager
    this.websocketManager.setUpdateStageCallback(this.updateStageCallback.bind(this))
  }

  entities = [];
  processAllButton = null

  begin() {
    this.grabElements()
    this.attachListeners()
    this.websocketManager.connect()
  }

  grabElements() {
    document.querySelectorAll(".entity")
      .forEach((entity, index) => {
        this.entities[index] = new Entity(
          entity.id,
          entity.querySelector(".entity-name"),
          entity.querySelector("input[type='checkbox']"),
          entity.querySelector("button[name='process-button']"),
          entity.querySelector(".status-label")
        )
      })

    this.processAllButton = document.querySelector("button[name='process-selected']")
  }

  attachListeners() {
    this.entities.forEach(entity => {
      entity.addStartButtonListener("click", (event) => this.startProcessingSingleEntity(event))
    })
    this.processAllButton.addEventListener("click", this.startProcessingSelectedEntities.bind(this))
  }

  startProcessingSelectedEntities() {
    const request = new ProcessEntitiesRequest()
    this.getSelectedEntities().forEach(entity => request.addEntity(entity.name))
    this.websocketManager.sendMessage(request)
  }

  startProcessingSingleEntity(event) {
    const parent = event.target.closest("tr")
    const entity = this.findEntityById(parent.id)
    entity.statusLabel.textContent = "???"
    this.websocketManager.sendMessage(new ProcessEntityRequest(entity.name))
  }


  findEntityById(id) {
    return this.entities
      .find(entity => entity.id === id)
  }

  findEntityByName(name) {
    return this.entities
      // * I know, suuuuper hacky
      .find(entity => entity.nameElement.textContent === name)
  }

  getSelectedEntities() {
    return this.entities.filter(entity => entity.isSelected === true)
  }

  // * yeah this is a hacky way of handing the data back up, but I'm just trying to rattle this out sooooooodealwithit
  updateStageCallback(entityName, color) {
    const entity = this.findEntityByName(entityName)
    entity.statusLabel.style.color = color
  }
}

window.addEventListener("DOMContentLoaded", () => {
  const socketManager = new WebsocketManager()
  const entityProcessor = window.ep = new EntityProcessor(socketManager)

  entityProcessor.begin()
})