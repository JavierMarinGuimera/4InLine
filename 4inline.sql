-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-05-2022 a las 18:40:09
-- Versión del servidor: 10.4.22-MariaDB
-- Versión de PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `4inline`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `scores`
--

CREATE TABLE `scores` (
  `id` int(6) NOT NULL,
  `user_id` int(6) NOT NULL,
  `score` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `scores`
--

INSERT INTO `scores` (`id`, `user_id`, `score`) VALUES
(1, 3, -122),
(2, 3, -121),
(3, 3, -121),
(4, 3, -120);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tokens`
--

CREATE TABLE `tokens` (
  `id` int(6) NOT NULL,
  `user_id` int(6) NOT NULL,
  `current_token` varchar(150) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tokens`
--

INSERT INTO `tokens` (`id`, `user_id`, `current_token`, `creation_date`) VALUES
(1, 3, 'GFSKWesLCmZQ3XkUq0ZEYmM-p2ElKWggQslMXKwsIYHQPB0kidAcC4uFtxsA762U3APagk1Lm47gzdfK3hjsMA==', '2022-05-23 14:35:13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(6) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(3, 'Javier', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `scores`
--
ALTER TABLE `scores`
  ADD PRIMARY KEY (`id`),
  ADD KEY `scores-users` (`user_id`);

--
-- Indices de la tabla `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tokens-users` (`user_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `scores`
--
ALTER TABLE `scores`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `scores`
--
ALTER TABLE `scores`
  ADD CONSTRAINT `scores-users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `tokens`
--
ALTER TABLE `tokens`
  ADD CONSTRAINT `tokens-users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
