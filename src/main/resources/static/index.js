const ResponseType = {
  STAGE_1: 'STAGE_1',
  STAGE_2: 'STAGE_2',
  STAGE_3: 'STAGE_3',
}


class Entity {
  startButton = null
  statusLabel = null

  constructor(startButtonElement, statusLabelElement) {
    this.startButton = startButtonElement
    this.statusLabel = statusLabelElement
  }

  updateLabel(value) {
    this.statusLabel.value = value
  }

  addStartButtonListener(event, callback) {
    this.startButton.addEventListener(event, callback)
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
    this.stompClient.send("/app/messages", {}, JSON.stringify({ "to": "user 2", "content": "test" }))
  }

  handleServerMessage(message) {
    switch (message.type)
    {
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

  begin() {
    this.grabElements()
    this.attachListeners()
    this.websocketManager.connect()
  }

  grabElements() {
    document.querySelectorAll(".entity")
      .forEach((entity, index) => {
        this.entities[index] = new Entity(entity.querySelector(".start-button"), entity.querySelector(".status-label"))
      })
  }

  attachListeners() {
    this.entities.forEach(entity => {
      entity.addStartButtonListener("click", () => {
        this.websocketManager.sendMessage("entity-1") // todo: come back and make this more concrete
      })
    })
  }
}

window.addEventListener("DOMContentLoaded", () => {
  const socketManager = new WebsocketManager()
  const entityProcessor = window.ep = new EntityProcessor(socketManager)

  entityProcessor.begin()
})