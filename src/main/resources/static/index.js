class Entity {
  startButton = null
  statusLabel = null

  constructor(startButtonElement, statusLabelElement) {
    this.startButton = startButtonElement
    this.statusLabel = statusLabelElement
  }
}

class WebsocketManager {
  stompClient = null

  connect() {
    let socket = new SocketJS("/ws")
    this.stompClient = Stomp.over(socket)
    this.stompClient.connect({}, this.applySubscriptions.bind(this))
  }

  applySubscriptions() {
    //  * right now let's do hard coded subscriptions, but really we should add an `addSubscription` 
    //  * method and then keep an array of subscriptions to apply

    this.stompClient.subscribe('/topic/messages', (messageData) => {
      console.log(messageData)
    })
  }

  sendMessage(message) {
    
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
  }

  grabElements() {
    document.querySelectorAll(".entity")
      .forEach((entity, index) => {
        this.entities[index] = Entity(entity.querySelector(".start-button"), entity.querySelector(".status-label"))
      })
  }

  attachListeners() {
    this.entities.forEach(entity => {
      entity.startButton.addEventListener("click", () => {
        this.websocketManager.sendMessage("entity-1") // todo: come back and make this more concrete
      })
    })
  }
}