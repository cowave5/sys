if('serviceWorker' in navigator) {window.addEventListener('load', () => {navigator.serviceWorker.register('/home/sw.js', { scope: '/' })})}
