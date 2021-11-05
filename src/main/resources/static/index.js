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

  connect() {
    let socket = new SockJS("/ws")
    this.stompClient = Stomp.over(socket)
    this.stompClient.connect({}, this.applySubscriptions.bind(this))
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
    switch (message.type) {
      case ResponseType.STAGE_1:
        console.log("stage 1")
        break
      case ResponseType.STAGE_2:
        console.log("stage 2")
        break
      case ResponseType.STAGE_3:
        console.log("stage 3")
        break
    }
  }
}

class EntityProcessor {
  websocketManager = null

  constructor(websocketManager) {
    this.websocketManager = websocketManager
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
    this.getSelectedEntities().map(entity => request.addEntity(entity.name))
    this.websocketManager.sendMessage(request)
  }

  startProcessingSingleEntity(event) {
    const parent = event.target.closest("tr")
    const entity = this.findEntityById(parent.id)
    this.websocketManager.sendMessage(new ProcessEntityRequest(entity.name))
  }


  findEntityById(id) {
    return this.entities
      .find(entity => entity.id === id)
  }

  getSelectedEntities() {
    return this.entities.filter(entity => entity.isSelected === true)
  }
}

window.addEventListener("DOMContentLoaded", () => {
  const socketManager = new WebsocketManager()
  const entityProcessor = window.ep = new EntityProcessor(socketManager)

  entityProcessor.begin()
})