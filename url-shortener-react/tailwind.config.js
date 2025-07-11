/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      backgroundImage: {
        "custom-gradient": "linear-gradient(135deg, #3b82f6 0%, #9333ea 100%)",
        "custom-gradient-2": "linear-gradient(135deg, #232526 0%, #3b82f6 100%)",
        "card-gradient": "linear-gradient(135deg, #232526 0%, #3b82f6 100%)",
        "3d-blob": "radial-gradient(circle at 60% 40%, #3b82f6 0%, #9333ea 100%)",
      },
      colors: {
        navbarColor: "#18181b",
        btnColor: "#7f5af0",
        linkColor: "#60a5fa",
        darkBg: "#18181b",
        darkCard: "#232526",
        darkText: "#f3f4f6",
        accent: "#9333ea",
        accent2: "#3b82f6",
        accent3: "#7f5af0",
        accent4: "#2cb67d",
        accent5: "#ff8906",
        accent6: "#f25f4c",
      },
      boxShadow: {
        custom: "0 0 15px rgba(0, 0, 0, 0.3)",
        right: "10px 0px 10px -5px rgba(0, 0, 0, 0.3)",
        '3d': '0 10px 30px 0 rgba(60,60,100,0.25), 0 1.5px 4px 0 rgba(80,80,120,0.15)',
      },
      fontFamily: {
        roboto: ["Roboto", "sans-serif"],
        montserrat: ["Montserrat"],
      },
    },
  },

  variants: {
    extend: {
      backgroundImage: ["responsive"],
    },
  },

  plugins: [],
};