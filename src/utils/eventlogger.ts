export const emitEvent = (eventName: string) => {
  fetch('http://localhost:8080/producer/event', {
    method: 'POST',
    headers: {
      'Content-Type': 'text/plain',
    },
    body: eventName,
  })
    .then((res) => {
      if (!res.ok) throw new Error('Failed to emit event');
      console.log('Event sent:', eventName);
    })
    .catch((err) => console.error(err));
};